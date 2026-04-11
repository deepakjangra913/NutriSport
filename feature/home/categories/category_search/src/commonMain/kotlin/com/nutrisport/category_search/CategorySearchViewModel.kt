package com.nutrisport.category_search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutrisport.data.domain.ProductsRepository
import com.nutrisport.shared.domain.ProductCategory
import com.nutrisport.shared.util.RequestState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

class CategorySearchViewModel(
    private val productsRepository: ProductsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val products = productsRepository.readProductsByCategoryFlow(
        ProductCategory.valueOf(savedStateHandle.get<String>("category").orEmpty())
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RequestState.Loading
    )

    private var _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    fun updateSearchQuery(value: String) {
        _searchQuery.value = value
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val filterProducts = searchQuery
        .debounce(500)
        .flatMapLatest { query ->
            if (query.isEmpty()) products
            else {
                if (products.value.isSuccess()) {
                    flowOf(
                        RequestState.Success(
                            products.value.getSuccessData()
                                .filter { it.title.lowercase().contains(query.lowercase()) })
                    )
                } else {
                    products
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.Loading
        )
}