package com.nutrisport.admin_panel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutrisport.data.domain.AdminRepository
import com.nutrisport.shared.util.RequestState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class AdminPanelViewModel(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val products = adminRepository.readLastTenProducts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.Loading
        )

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val filteredProducts = searchQuery
        .debounce(500)
        .flatMapLatest { query ->
            if (query.isEmpty()) products
            else adminRepository.searchProductByTitle(query)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(500),
            initialValue = RequestState.Loading
        )

    fun updatedSearchQuery(text: String) {
        _searchQuery.value = text
    }
}