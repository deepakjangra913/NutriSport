package com.nutrisport.shared.domain

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class Order(
    val id: String = Uuid.random().toHexString(),
    val customerId: String,
    val items: List<CartItem>,
    val totalAmount: Double,
    val token: String? = null
)
