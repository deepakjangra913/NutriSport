package com.nutrisport.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutrisport.data.domain.CustomerRepository
import com.nutrisport.data.domain.ProductsRepository
import com.nutrisport.shared.domain.CartItem
import com.nutrisport.shared.util.RequestState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val productsRepository: ProductsRepository,
    private val customerRepository: CustomerRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val product = productsRepository.readProductByIdFlow(
        savedStateHandle.get<String>("id").orEmpty()
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RequestState.Loading
    )

    var quantity by mutableStateOf(1)
        private set

    fun updateQuantity(value: Int) {
        quantity = value
    }

    var selectedFlavour by mutableStateOf<String?>(null)
        private set

    fun updateFlavour(value: String) {
        selectedFlavour = value
    }

    fun addItemToCart(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) = viewModelScope.launch {
        val productId = savedStateHandle.get<String>("id")
        if (productId != null) {
            customerRepository.addItemToCart(
                cartItem = CartItem(
                    productId = productId,
                    flavor = selectedFlavour,
                    quantity = quantity
                ),
                onSuccess = onSuccess,
                onError = onError
            )
        } else {
            onError("Product Id not found")
        }
    }
}