package com.nutrisport.data.domain

import com.nutrisport.shared.domain.Product
import dev.gitlive.firebase.storage.File

interface AdminRepository {

    fun getCustomerUserId(): String?

    suspend fun createProduct(
        product: Product,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    suspend fun uploadImageToStorage(file: File): String?
}