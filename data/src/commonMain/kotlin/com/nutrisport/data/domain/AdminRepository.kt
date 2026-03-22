package com.nutrisport.data.domain

import com.nutrisport.shared.domain.Product

interface AdminRepository {

    fun getCustomerUserId(): String?

    suspend fun createProduct(
        product: Product,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )
}