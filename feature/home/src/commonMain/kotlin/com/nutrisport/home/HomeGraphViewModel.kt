package com.nutrisport.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutrisport.data.domain.CustomerRepository
import com.nutrisport.data.domain.ProductsRepository
import com.nutrisport.shared.util.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeGraphViewModel(
    private val customerRepository: CustomerRepository,
    private val productsRepository: ProductsRepository
) : ViewModel() {

    val customerData = customerRepository.readCustomerFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.Loading
        )

    private val customer = customerRepository.readCustomerFlow()

    fun signOut(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                customerRepository.signOut()
            }
            if (result.isSuccess()) {
                onSuccess()
            } else if (result.isError()) {
                onError(result.getErrorMessage())
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val products = customer.flatMapLatest { customerState ->
        if (customerState.isSuccess()) {
            val productIds = customerState.getSuccessData().cart.map {
                it.productId
            }.toSet()
            if (productIds.isNotEmpty()) {
                productsRepository.readProductByIdsFlow(productIds.toList())
            } else {
                flowOf(RequestState.Success(emptyList()))
            }
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

    @OptIn(ExperimentalCoroutinesApi::class)
    val totalAmountFlow = cartItemsWithProducts
        .flatMapLatest { data ->
            if (data.isSuccess()) {
                val items = data.getSuccessData()
                val totalPrice = items.sumOf { (cartItem, product) ->
                    product.price * cartItem.quantity
                }
                flowOf(RequestState.Success(totalPrice))
            } else if (data.isError()) {
                flowOf(RequestState.Error(data.getErrorMessage()))
            }else{
                flowOf(RequestState.Loading)
            }
        }
}