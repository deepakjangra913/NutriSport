package com.nutrisport.manage_product

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutrisport.data.domain.AdminRepository
import com.nutrisport.shared.domain.Product
import com.nutrisport.shared.domain.ProductCategory
import com.nutrisport.shared.util.RequestState
import dev.gitlive.firebase.storage.File
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * UI state used by [ManageProductViewModel] to manage product creation form data.
 *
 * This state holds all user-entered values required to create a product,
 * including basic details such as title, description, thumbnail, category,
 * flavours, weight, and price.
 *
 * @property id Unique identifier generated for the product by default.
 * @property title Product title entered by the user.
 * @property description Product description entered by the user.
 * @property thumbnail Thumbnail URL or placeholder value for the product image.
 * @property category Selected [ProductCategory] for the product.
 * @property flavours Comma-separated flavours entered as a raw string.
 * @property weight Optional product weight.
 * @property price Product price.
 */
data class ManageProductState @OptIn(ExperimentalUuidApi::class) constructor(
    val id: String = Uuid.random().toHexString(),
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val title: String = "",
    val description: String = "",
    val thumbnail: String = "Thumbnail",
    val category: ProductCategory = ProductCategory.Protein,
    val flavours: String = "",
    val weight: Int? = null,
    val price: Double = 0.0,
    val isNew: Boolean = false,
    val isPopular: Boolean = false,
    val isDiscounted: Boolean = false
)

/**
 * ViewModel responsible for managing product creation.
 *
 * This ViewModel:
 * - Maintains the product form state
 * - Validates whether the form is ready for submission
 * - Handles thumbnail upload state
 * - Creates a new product through [AdminRepository]
 *
 * @property adminRepository Repository used for product creation and image upload operations.
 */
class ManageProductViewModel(
    private val adminRepository: AdminRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productId = savedStateHandle.get<String?>("id").orEmpty()

    /**
     * Current UI state of the manage product screen.
     *
     * This state is updated whenever the user changes any field in the form.
     */
    var screenState by mutableStateOf(ManageProductState())
        private set

    /**
     * Represents the current state of the thumbnail upload request.
     *
     * This is used by the UI to show idle, loading, success, or error states
     * during thumbnail upload.
     */
    var thumbnailUploaderState: RequestState<Unit> by mutableStateOf(RequestState.Idle)
        private set

    /**
     * Indicates whether the product form contains the minimum required data.
     *
     * The form is considered valid when:
     * - title is not empty
     * - description is not empty
     * - thumbnail is not empty
     * - price is not zero
     */
    val isFormValid: Boolean
        get() = screenState.title.isNotEmpty() &&
                screenState.description.isNotEmpty() &&
                screenState.thumbnail.isNotEmpty() &&
                screenState.price != 0.0

    init {
        productId.takeIf { it.isNotEmpty() }?.let { id ->
            viewModelScope.launch {
                val selectedProduct = adminRepository.readProductById(id)
                if (selectedProduct.isSuccess()) {
                    val product = selectedProduct.getSuccessData()
                    updateId(product.id)
                    updateCreatedAt(product.createdAt)
                    updateTitle(product.title)
                    updateDescription(product.description)
                    updateCategory(ProductCategory.valueOf(product.category))
                    updateWeight(product.weight)
                    updateFlavours(product.flavours?.joinToString(",").orEmpty())
                    updatePrice(product.price)
                    updateThumbnail(product.thumbnail)
                    updateNew(product.isNew)
                    updatePopular(product.isPopular)
                    updateDiscounted(product.isDiscounted)
                    updateThumbnailUploaderState(RequestState.Success(Unit))
                }
            }
        }
    }

    fun updateId(id: String) {
        screenState = screenState.copy(id = id)
    }

    fun updateCreatedAt(value: Long) {
        screenState = screenState.copy(createdAt = value)
    }

    /**
     * Updates the product title in the current screen state.
     *
     * @param title New title entered by the user.
     */
    fun updateTitle(title: String) {
        screenState = screenState.copy(title = title)
    }

    /**
     * Updates the product description in the current screen state.
     *
     * @param description New description entered by the user.
     */
    fun updateDescription(description: String) {
        screenState = screenState.copy(description = description)
    }

    /**
     * Updates the product thumbnail in the current screen state.
     *
     * This is typically called after a thumbnail is uploaded successfully
     * and a download URL is received.
     *
     * @param thumbnail Thumbnail URL or value to store.
     */
    fun updateThumbnail(thumbnail: String) {
        screenState = screenState.copy(thumbnail = thumbnail)
    }

    /**
     * Updates the selected product category in the current screen state.
     *
     * @param category Selected [ProductCategory].
     */
    fun updateCategory(category: ProductCategory) {
        screenState = screenState.copy(category = category)
    }

    /**
     * Updates the flavours value in the current screen state.
     *
     * Flavours are stored as a comma-separated string and later converted
     * into a list while creating the product.
     *
     * @param value Comma-separated flavours string.
     */
    fun updateFlavours(value: String) {
        screenState = screenState.copy(flavours = value)
    }

    /**
     * Updates the product weight in the current screen state.
     *
     * @param weight Product weight, or null if not provided.
     */
    fun updateWeight(weight: Int?) {
        screenState = screenState.copy(weight = weight)
    }

    /**
     * Updates the product price in the current screen state.
     *
     * @param price Product price entered by the user.
     */
    fun updatePrice(price: Double) {
        screenState = screenState.copy(price = price)
    }

    fun updateNew(value: Boolean) {
        screenState = screenState.copy(isNew = value)
    }

    fun updatePopular(value: Boolean) {
        screenState = screenState.copy(isPopular = value)
    }

    fun updateDiscounted(value: Boolean) {
        screenState = screenState.copy(isDiscounted = value)
    }

    /**
     * Updates the thumbnail uploader request state.
     *
     * @param value New upload state.
     */
    fun updateThumbnailUploaderState(value: RequestState<Unit>) {
        thumbnailUploaderState = value
    }

    /**
     * Creates a new product using the current form state.
     *
     * This function converts the current [screenState] into a [Product] object
     * and sends it to [adminRepository] for persistence.
     *
     * Default flags such as `isPopular`, `isDiscounted`, and `isNew`
     * are set to false during creation.
     *
     * @param onSuccess Callback invoked when the product is created successfully.
     * @param onError Callback invoked when product creation fails with an error message.
     */
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
                isPopular = screenState.isPopular,
                isDiscounted = screenState.isDiscounted,
                isNew = screenState.isNew,
            ),
            onSuccess = onSuccess,
            onError = onError
        )
    }

    /**
     * Uploads a thumbnail image file to storage and updates the screen state
     * with the returned download URL.
     *
     * This function:
     * - Validates the selected file
     * - Updates [thumbnailUploaderState] to loading
     * - Uploads the image through [adminRepository]
     * - Stores the received download URL in [screenState]
     * - Updates the upload state to success or error
     *
     * @param file Image file selected by the user.
     * @param onSuccess Callback invoked after a successful upload.
     */
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

            productId.takeIf { it.isNotEmpty() }?.let { id ->
                adminRepository.updateProductThumbnail(
                    productId = id,
                    downloadUrl = downloadUrl,
                    onSuccess = {
                        // Image has been successfully updated
                        onSuccess()
                        updateThumbnail(downloadUrl)
                        updateThumbnailUploaderState(RequestState.Success(Unit))
                    },
                    onError = { message ->
                        updateThumbnailUploaderState(RequestState.Error(message))
                    }
                )
            } ?: run {

                // Image has been successfully uploaded
                onSuccess()
                updateThumbnail(downloadUrl)
                updateThumbnailUploaderState(RequestState.Success(Unit))
            }
        } catch (e: Exception) {
            updateThumbnailUploaderState(RequestState.Error("Error while uploading: ${e.message}"))
        }
    }

    fun updateProduct(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) = viewModelScope.launch {
        if (isFormValid) {
            adminRepository.updateProduct(
                product = Product(
                    id = screenState.id,
                    createdAt = screenState.createdAt,
                    title = screenState.title,
                    description = screenState.description,
                    thumbnail = screenState.thumbnail,
                    category = screenState.category.name,
                    flavours = screenState.flavours.split(",").map {
                        it.trim()
                    }.filter { it.isNotEmpty() },
                    weight = screenState.weight,
                    price = screenState.price,
                    isPopular = screenState.isPopular,
                    isDiscounted = screenState.isDiscounted,
                    isNew = screenState.isNew
                ),
                onSuccess = onSuccess,
                onError = onError
            )
        } else {
            onError("Please fill in the information.")
        }
    }

    fun deleteThumbnailFromStorage(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) = viewModelScope.launch {
        viewModelScope.launch {
            adminRepository.deleteImageFromStorage(
                downloadUrl = screenState.thumbnail,
                onSuccess = {
                    productId.takeIf { it.isNotEmpty() }?.let { id ->
                        viewModelScope.launch {
                            adminRepository.updateProductThumbnail(
                                productId = id,
                                downloadUrl = "",
                                onSuccess = {
                                    updateThumbnail(
                                        thumbnail = ""
                                    )
                                    updateThumbnailUploaderState(RequestState.Idle)
                                    onSuccess()
                                },
                                onError = { message -> onError(message) }
                            )
                        }
                    } ?: run {
                        updateThumbnail(thumbnail = "")
                        updateThumbnailUploaderState(RequestState.Idle)
                        onSuccess()
                    }
                },
                onError = onError
            )
        }
    }

    fun deleteProduct(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) = viewModelScope.launch {
        productId.takeIf { it.isNotEmpty() }?.let { id ->
            adminRepository.deleteProduct(
                productId = id,
                onSuccess = {
                    deleteThumbnailFromStorage(
                        onSuccess = {},
                        onError = { message -> }
                    )
                    onSuccess()
                },
                onError = { message ->
                    onError(message)
                }
            )
        }
    }
}