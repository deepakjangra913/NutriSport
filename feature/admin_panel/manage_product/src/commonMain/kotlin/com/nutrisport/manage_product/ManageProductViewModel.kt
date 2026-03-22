package com.nutrisport.manage_product

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutrisport.data.domain.AdminRepository
import com.nutrisport.shared.domain.Product
import com.nutrisport.shared.domain.ProductCategory
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class ManageProductState(
    val id: String = Uuid.random().toHexString(),
    val title: String = "",
    val description: String = "",
    val thumbnail: String = "Thumbnail",
    val category: ProductCategory = ProductCategory.Protein,
    val flavours: String = "",
    val weight: Int? = null,
    val price: Double = 0.0,
)

class ManageProductViewModel(
    private val adminRepository: AdminRepository
) : ViewModel() {

    var screenState by mutableStateOf(ManageProductState())
        private set

    val isFormValid: Boolean
        get() = screenState.title.isNotEmpty() &&
                screenState.description.isNotEmpty() &&
                screenState.thumbnail.isNotEmpty() &&
                screenState.price != 0.0

    fun updateTitle(title: String) {
        screenState = screenState.copy(title = title)
    }

    fun updateDescription(description: String) {
        screenState = screenState.copy(description = description)
    }

    fun updateThumbnail(thumbnail: String) {
        screenState = screenState.copy(thumbnail = thumbnail)
    }

    fun updateCategory(category: ProductCategory) {
        screenState = screenState.copy(category = category)
    }

    fun updateFlavours(value: String) {
        screenState = screenState.copy(flavours = value)
    }

    fun updateWeight(weight: Int?) {
        screenState = screenState.copy(weight = weight)
    }

    fun updatePrice(price: Double) {
        screenState = screenState.copy(price = price)
    }

    fun createProduct(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) = viewModelScope.launch {
        adminRepository.createProduct(
            product = Product(
                id = screenState.id,
                title = screenState.title,
                description = screenState.description,
                thumbnail = screenState.thumbnail,
                category = screenState.category.name,
                flavours = screenState.flavours.split(","),
                weight = screenState.weight,
                price = screenState.price,
                isPopular = false,
                isDiscounted = false,
                isNew = false,
            ),
            onSuccess = onSuccess,
            onError = onError
        )
    }
}