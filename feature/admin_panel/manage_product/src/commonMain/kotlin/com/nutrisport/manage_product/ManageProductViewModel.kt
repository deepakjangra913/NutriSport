package com.nutrisport.manage_product

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutrisport.data.domain.AdminRepository
import com.nutrisport.shared.domain.Product
import com.nutrisport.shared.domain.ProductCategory
import com.nutrisport.shared.util.RequestState
import dev.gitlive.firebase.storage.File
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

    var thumbnailUploaderState: RequestState<Unit> by mutableStateOf(RequestState.Idle)
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

    fun updateThumbnailUploaderState(value: RequestState<Unit>) {
        thumbnailUploaderState = value
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

    fun uploadThumbnailToStorage(
        file: File?,
        onSuccess: () -> Unit
    ) = viewModelScope.launch {
        if (file == null) {
            updateThumbnailUploaderState(RequestState.Error("File is null. Error while selecting the image"))
            return@launch
        }

        // Loading state
        updateThumbnailUploaderState(RequestState.Loading)

        try {
            val downloadUrl = adminRepository.uploadImageToStorage(file)
            if (downloadUrl.isNullOrEmpty()) {
                throw Exception("Failed to retrieve a download URL after the upload.")
            }

            // Image has been successfully uploaded
            onSuccess()
            updateThumbnail(downloadUrl)
            updateThumbnailUploaderState(RequestState.Success(Unit))
        } catch (e: Exception) {
            updateThumbnailUploaderState(RequestState.Error("Error while uploading: ${e.message}"))
        }
    }
}