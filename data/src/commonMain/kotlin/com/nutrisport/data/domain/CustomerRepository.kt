package com.nutrisport.data.domain

import com.nutrisport.shared.domain.Customer
import com.nutrisport.shared.util.RequestState
import dev.gitlive.firebase.auth.FirebaseUser

interface CustomerRepository {
    suspend fun createCustomer(
        user: FirebaseUser?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    )

    suspend fun signOut(): RequestState<Unit>
    suspend fun getCurrentUser(): Customer?

    fun getCurrentUserId(): String?
}