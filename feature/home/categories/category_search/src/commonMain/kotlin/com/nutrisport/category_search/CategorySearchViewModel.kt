package com.nutrisport.category_search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutrisport.data.domain.ProductsRepository
import com.nutrisport.shared.domain.ProductCategory
import com.nutrisport.shared.util.RequestState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class CategorySearchViewModel(
    private val productsRepository: ProductsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val products = productsRepository.readProductsByCategoryFlow(
        ProductCategory.valueOf(savedStateHandle.get<String>("category").orEmpty())
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RequestState.Loading
    )
}