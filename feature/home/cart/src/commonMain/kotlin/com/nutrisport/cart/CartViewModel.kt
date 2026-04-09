package com.nutrisport.cart

import androidx.lifecycle.ViewModel
import com.nutrisport.data.domain.CustomerRepository
import com.nutrisport.data.domain.ProductsRepository
import com.nutrisport.shared.util.RequestState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class CartViewModel(
    private val customerRepository: CustomerRepository,
    private val productsRepository: ProductsRepository
) : ViewModel() {

    private val customer = customerRepository.readCustomerFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val products = customer.flatMapLatest { customerState ->
        if (customerState.isSuccess()) {
            val productIds = customerState.getSuccessData().cart.map {
                it.productId
            }.toSet()
            productsRepository.readProductByIdsFlow(productIds.toList())
        } else if (customerState.isError()) {
            flowOf(RequestState.Error(customerState.getErrorMessage()))
        } else flowOf(RequestState.Loading)
    }

    val cartItemsWithProducts = combine(customer, products) { customerState, productState ->
        when {
            customerState.isSuccess() && productState.isSuccess() -> {
                val cart = customerState.getSuccessData().cart
                val products = productState.getSuccessData()

                val result = cart.mapNotNull { cartItem ->
                    val product = products.find { it.id == cartItem.productId }
                    product?.let { cartItem to it }
                }

                RequestState.Success(result)
            }

            customerState.isError() -> RequestState.Error(customerState.getErrorMessage())
            productState.isError() -> RequestState.Error(productState.getErrorMessage())

            else -> RequestState.Loading
        }
    }
}