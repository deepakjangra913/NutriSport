package com.nutrisport.data

import com.nutrisport.data.domain.AdminRepository
import com.nutrisport.shared.domain.Product
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore

class AdminRepositoryImpl : AdminRepository {
    override fun getCustomerUserId(): String? = Firebase.auth.currentUser?.uid

    override suspend fun createProduct(
        product: Product,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val currentUserId = getCustomerUserId()
            if (currentUserId != null) {
                val firestore = Firebase.firestore
                val productCollection = firestore.collection("products")
                productCollection.document(product.id).set(product)
                onSuccess()
            } else {
                onError("User is not available")
            }

        } catch (ex: Exception) {
            onError("Error while creating new product: ${ex.message}")
        }
    }
}