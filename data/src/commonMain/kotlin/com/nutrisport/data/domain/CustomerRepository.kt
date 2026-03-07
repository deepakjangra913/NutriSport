package com.nutrisport.data.domain

import com.nutrisport.shared.domain.Customer
import dev.gitlive.firebase.auth.FirebaseUser

interface CustomerRepository {
    suspend fun createCustomer(
        user: FirebaseUser?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    )

    suspend fun getCurrentUser(uid: String): Customer?

    fun getCurrentUserId(): String?
}