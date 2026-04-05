package com.nutrisport.products_overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutrisport.data.domain.ProductsRepository
import com.nutrisport.shared.util.RequestState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class ProductsOverviewViewModel(
    private val productsRepository: ProductsRepository
) : ViewModel() {

    val discountedProducts = productsRepository.readDiscountedProducts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.Loading
        )
}