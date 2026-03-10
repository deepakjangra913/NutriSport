package com.nutrisport.data

import com.nutrisport.data.domain.CustomerRepository
import com.nutrisport.shared.domain.Customer
import com.nutrisport.shared.util.RequestState
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
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
                            customer.copy(
                                updatedAt = currentTime,
                                createdAt = existingUser?.createdAt
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
                    onSuccess()
                }
            } else {
                onError("User is not available.")
            }
        } catch (e: Exception) {
            onError("Error while creating a Customer: ${e.message}")
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
}