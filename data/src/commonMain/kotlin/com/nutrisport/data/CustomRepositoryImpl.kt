package com.nutrisport.data

import com.nutrisport.data.domain.CustomerRepository
import com.nutrisport.shared.domain.CartItem
import com.nutrisport.shared.domain.Customer
import com.nutrisport.shared.util.RequestState
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlin.time.Clock

class CustomRepositoryImpl : CustomerRepository {

    override suspend fun createCustomer(
        user: FirebaseUser?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            if (user != null) {
                val customerCollection = Firebase.firestore.collection(collectionPath = "customer")
                val customer = Customer(
                    id = user.uid,
                    firstName = user.displayName?.split(" ")?.firstOrNull() ?: "Unknown",
                    lastName = user.displayName?.split(" ")?.lastOrNull() ?: "Unknown",
                    email = user.email ?: "Unknown",
                )

                val customerExist = customerCollection.document(user.uid).get().exists
                val currentTime = Clock.System.now().toEpochMilliseconds()
                if (customerExist) {

                    // Updating the updated time
                    val existingUser = getCurrentUser()
                    customerCollection.document(user.uid)
                        .update(
                            mapOf(
                                "updatedAt" to currentTime
                            )
                        )
                    onSuccess()
                } else {

                    customerCollection.document(user.uid).set(
                        customer.copy(
                            createdAt = currentTime,
                            updatedAt = currentTime
                        )
                    )

                    customerCollection.document(user.uid)
                        .collection("privateData")
                        .document("role")
                        .set(mapOf("isAdmin" to false))
                    onSuccess()
                }
            } else {
                onError("User is not available.")
            }
        } catch (e: Exception) {
            onError("Error while creating a Customer: ${e.message}")
        }
    }

    override fun readCustomerFlow(): Flow<RequestState<Customer>> = channelFlow {
        try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val database = Firebase.firestore
                database
                    .collection("customer")
                    .document(userId)
                    .snapshots.collectLatest { document ->
                        if (document.exists) {
                            val privateDataDocument =
                                database.collection(collectionPath = "customer")
                                    .document(userId)
                                    .collection("privateData")
                                    .document("role")
                                    .get()

                            val customer = Customer(
                                id = document.id,
                                firstName = document.get("firstName"),
                                lastName = document.get("lastName"),
                                email = document.get("email"),
                                city = document.get("city"),
                                postalCode = document.get("postalCode"),
                                address = document.get("address"),
                                phoneNumber = document.get("phoneNumber"),
                                cart = document.get("cart"),
                                isAdmin = privateDataDocument.get("isAdmin")
                            )
                            send(RequestState.Success(customer))
                        } else {
                            send(RequestState.Error("Queried customer document does not exist."))
                        }
                    }
            } else {
                send(RequestState.Error("User is not available."))
            }
        } catch (ex: Exception) {
            send(RequestState.Error("Error while reading customer information: ${ex.message}"))
        }
    }

    override suspend fun signOut(): RequestState<Unit> {
        return try {
            Firebase.auth.signOut()
            RequestState.Success(data = Unit)
        } catch (e: Exception) {
            RequestState.Error("Error while signing out: ${e.message}")
        }
    }

    override fun getCurrentUserId(): String? {
        return Firebase.auth.currentUser?.uid
    }

    override suspend fun getCurrentUser(): Customer? {
        val user = Firebase.auth.currentUser ?: return null

        return Firebase.firestore
            .collection("customer")
            .document(user.uid)
            .get()
            .data<Customer>()
    }

    override suspend fun updateCustomer(
        customer: Customer,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val firestore = Firebase.firestore
                val customerCollection = firestore.collection("customer")
                val existingCustomer = customerCollection.document(customer.id).get()
                val currentTime = Clock.System.now().toEpochMilliseconds()
                if (existingCustomer.exists) {
                    customerCollection.document(customer.id)
                        .update(
                            "firstName" to customer.firstName,
                            "lastName" to customer.lastName,
                            "city" to customer.city,
                            "postalCode" to customer.postalCode,
                            "address" to customer.address,
                            "phoneNumber" to customer.phoneNumber,
                            "updatedAt" to currentTime
                        )
                    onSuccess()
                } else {
                    onError("Customer not found.")
                }
            } else {
                onError("User is not available")
            }
        } catch (ex: Exception) {
            onError("Error while updating a Customer Information: ${ex.message}")
        }
    }

    override suspend fun addItemToCart(
        cartItem: CartItem,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val currentUserId = getCurrentUserId()
            if (currentUserId != null) {
                val database = Firebase.firestore
                val customerCollection = database.collection(collectionPath = "customer")

                val existingCustomer = customerCollection.document(currentUserId).get()
                if (existingCustomer.exists) {
                    val existingCart = existingCustomer.get<List<CartItem>>("cart")
                    val updatedCart = existingCart + cartItem
                    customerCollection.document(currentUserId)
                        .set(
                            data = mapOf("cart" to updatedCart),
                            merge = true
                        )

                    onSuccess()
                } else {
                    onError("Select customer does not exist.")
                }
            } else {
                onError("User is not available")
            }
        } catch (ex: Exception) {
            onError("Error while adding a product to cart: ${ex.message}")
        }
    }

    override suspend fun updateCartItemQuantity(
        id: String,
        quantity: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val currentUserId = getCurrentUserId()
            if (currentUserId != null) {
                val database = Firebase.firestore
                val customerCollection = database.collection(collectionPath = "customer")

                val existingCustomer = customerCollection.document(currentUserId).get()
                if (existingCustomer.exists) {
                    val existingCart = existingCustomer.get<List<CartItem>>("cart")
                    val updatedCart = existingCart.map { cartItem ->
                        if (cartItem.id == id) {
                            cartItem.copy(quantity = quantity)
                        } else {
                            cartItem
                        }
                    }
                    customerCollection.document(currentUserId)
                        .update(
                            data = mapOf("cart" to updatedCart)
                        )

                    onSuccess()
                } else {
                    onError("Select customer does not exist.")
                }
            } else {
                onError("User is not available")
            }
        } catch (ex: Exception) {
            onError("Error while updating a product to cart: ${ex.message}")
        }
    }

    override suspend fun deleteCartItem(
        id: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val currentUserId = getCurrentUserId()
            if (currentUserId != null) {
                val database = Firebase.firestore
                val customerCollection = database.collection(collectionPath = "customer")

                val existingCustomer = customerCollection.document(currentUserId).get()
                if (existingCustomer.exists) {
                    val existingCart = existingCustomer.get<List<CartItem>>("cart")
                    val updatedCart = existingCart.filterNot { it.id == id }
                    customerCollection.document(currentUserId)
                        .update(
                            data = mapOf("cart" to updatedCart)
                        )

                    onSuccess()
                } else {
                    onError("Select customer does not exist.")
                }
            } else {
                onError("User is not available")
            }
        } catch (ex: Exception) {
            onError("Error while updating a product to cart: ${ex.message}")
        }
    }

    override suspend fun deleteAllCartItems(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val currentUserId = getCurrentUserId()
            if (currentUserId != null) {
                val database = Firebase.firestore
                val customerCollection = database.collection(collectionPath = "customer")

                val existingCustomer = customerCollection.document(currentUserId).get()
                if (existingCustomer.exists) {
                    customerCollection.document(currentUserId)
                        .update(
                            data = mapOf("cart" to emptyList<List<CartItem>>())
                        )
                    onSuccess()
                } else {
                    onError("Select customer does not exist.")
                }
            } else {
                onError("User is not available")
            }
        } catch (ex: Exception) {
            onError("Error while deleting all product from cart: ${ex.message}")
        }
    }
}