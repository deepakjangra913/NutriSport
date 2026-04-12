/**
 * Import function triggers from their respective submodules:
 *
 * const {onCall} = require("firebase-functions/v2/https");
 * const {onDocumentWritten} = require("firebase-functions/v2/firestore");
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */

// Firestore trigger for new orders
const { onDocumentCreated } = require("firebase-functions/v2/firestore");
const admin = require("firebase-admin");

admin.initializeApp();
const db = admin.firestore();

exports.createEmailDocument = onDocumentCreated("orders/{orderId}", async (event) => {
    const snapshot = event.data;
    const order = snapshot ? snapshot.data() : null;

    if (!order) {
      console.log("🔴 No order data found.");
      return;
    }

    console.log("🟢 New order received:", order);

    try {
      // Fetch customer details
      const customerDoc = await db.collection("customer").doc(order.customerId).get();
      const customer = customerDoc.exists ? customerDoc.data() : null;

      if (!customer) {
        console.warn(`🔴 Customer with ID ${order.customerId} not found.`);
      }

       // Fetch product details
      const productPromises = order.items.map(async (item) => {
        const productDoc = await db.collection("products").doc(item.productId).get();
  
        if (!productDoc.exists) {
          console.warn(`🔴 Product with ID ${item.productId} not found.`);
          return null;
        }

        return productDoc.data();
      });

      const productDetails = await Promise.all(productPromises);

      // Enhance order items with product data
      const cartItemsHtml = order.items
        .map((item, index) => {
          const product = productDetails[index];

          return `<li>
            <strong>${product ? product.title.toUpperCase() : "Unknown Product"}</strong>
            (${item.flavor ? item.flavor : "No Flavor"}) - $${product.price.toFixed(2)} x${item.quantity}
          </li>`;
        })
        .join("");

      const paymentMethod = order.token ? `PAY WITH PAYPAL (${order.token})` : "PAY ON DELIVERY";

      const emailData = {
        to: ["deepakjangra91313@gmail.com"],
        message: {
          subject: `🎉 New Order Received (${order.id})`,
          html: `
            <h2>🛒 Cart:</h2>
            <ul>${cartItemsHtml}</ul>
            <h2>💰 Earnings:</h2>
            <p><strong>Total:</strong> $${order.totalAmount.toFixed(2)}</p>
	    	<h2>💳 Payment Method:</h2>
            <p><strong>${paymentMethod}</strong></p>
            <h2>👋 Customer Information:</h2>
            <p><strong>First name:</strong> ${customer ? customer.firstName : "N/A"}</p>
            <p><strong>Last name:</strong> ${customer ? customer.lastName : "N/A"}</p>
            <p><strong>Email:</strong> ${customer ? customer.email : "N/A"}</p>
            <p><strong>City:</strong> ${customer ? customer.city : "N/A"}</p>
            <p><strong>Postal Code:</strong> ${customer ? customer.postalCode : "N/A"}</p>
            <p><strong>Address:</strong> ${customer ? customer.address : "N/A"}</p>
            <p><strong>Phone:</strong> ${customer && customer.phoneNumber ? `+${customer.phoneNumber.dialCode} ${customer.phoneNumber.number}` 
    : "N/A"}</p>
          `,
        },
      };

      // Add the email request to the mail collection
      await db.collection("mail").add(emailData);
      console.log("🟢 Mail document added to the collection successfully.");
    } catch (error) {
      console.error("🔴 Error while trying to create a new mail document:", error);
    }

    return null;
  });