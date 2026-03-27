package com.nutrisport.manage_product

import ContentWithMessageBar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.nutrisport.shared.BebasNeueFont
import com.nutrisport.shared.BorderIdle
import com.nutrisport.shared.ButtonPrimary
import com.nutrisport.shared.FontSize
import com.nutrisport.shared.IconPrimary
import com.nutrisport.shared.Resources
import com.nutrisport.shared.Surface
import com.nutrisport.shared.SurfaceLighter
import com.nutrisport.shared.TextPrimary
import com.nutrisport.shared.TextSecondary
import com.nutrisport.shared.component.AlertTextField
import com.nutrisport.shared.component.CustomTextField
import com.nutrisport.shared.component.ErrorCard
import com.nutrisport.shared.component.LoadingCard
import com.nutrisport.shared.component.PrimaryButton
import com.nutrisport.shared.component.dialog.CategoryPickerDialog
import com.nutrisport.shared.domain.ProductCategory
import com.nutrisport.shared.util.DisplayResult
import com.nutrisport.shared.util.RequestState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState

/**
 * Displays the product management screen used for creating a new product
 * or updating an existing one.
 *
 * This screen lets the user:
 * - upload a product thumbnail
 * - enter product details such as title, description, category, weight,
 *   flavours, and price
 * - validate the form before submission
 * - create or update the product using the associated ViewModel
 *
 * A category picker dialog is shown when the category field is tapped,
 * and thumbnail upload state is rendered using [RequestState] through
 * [DisplayResult].
 *
 * @param id Optional product id. When `null`, the screen behaves as an
 * add-product screen. When non-null, it behaves as an edit-product screen.
 * @param navigateBack Callback invoked when the top app bar back button is pressed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageProductScreen(
    id: String? = null,
    navigateBack: () -> Unit
) {

    var selectedCategory by remember { mutableStateOf(ProductCategory.Protein) }
    val messageBarState = rememberMessageBarState()
    var showCategoriesShow by remember { mutableStateOf(false) }
    val viewModel = koinViewModel<ManageProductViewModel>()
    val screenState = viewModel.screenState
    val isFormValid = viewModel.isFormValid
    val thumbnailUploaderState = viewModel.thumbnailUploaderState
    val photoPicker = koinInject<PhotoPicker>()
    photoPicker.InitializePhotoPicker { selectedFile ->
        viewModel.uploadThumbnailToStorage(
            file = selectedFile,
            onSuccess = {
                messageBarState.addSuccess("Thumbnail uploaded successfully")
            })
    }

    AnimatedVisibility(
        visible = showCategoriesShow
    ) {
        CategoryPickerDialog(
            category = screenState.category,
            onDismiss = {
                showCategoriesShow = false
            },
            onConfirmClick = { category ->
                selectedCategory = category
                viewModel.updateCategory(category)
                showCategoriesShow = false
            }
        )
    }

    Scaffold(
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (id == null) "New Product" else "Edit Product",
                        fontSize = FontSize.LARGE,
                        fontFamily = BebasNeueFont(),
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navigateBack()
                    }) {
                        Icon(
                            painter = painterResource(Resources.Icon.BackArrow),
                            contentDescription = "Black Arrow Icon",
                            tint = IconPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Surface,
                    scrolledContainerColor = Surface,
                    navigationIconContentColor = IconPrimary,
                    titleContentColor = TextPrimary,
                    actionIconContentColor = IconPrimary
                ),
            )
        }
    ) { padding ->
        ContentWithMessageBar(
            modifier = Modifier.padding(padding),
            messageBarState = messageBarState,
            contentBackgroundColor = Surface,
            errorMaxLines = 2
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 12.dp, bottom = 24.dp)
                    .imePadding()
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                width = 1.dp,
                                color = BorderIdle,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable(
                                enabled = thumbnailUploaderState.isIdle(),
                                onClick = {
                                    photoPicker.open()
                                }
                            )
                            .background(color = SurfaceLighter),
                        contentAlignment = Alignment.Center
                    ) {
                        thumbnailUploaderState.DisplayResult(
                            onIdle = {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    painter = painterResource(Resources.Icon.Plus),
                                    contentDescription = "Image add icon",
                                    tint = IconPrimary
                                )
                            },
                            onLoading = {
                                LoadingCard(modifier = Modifier.fillMaxSize())
                            },
                            onSuccess = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.TopEnd
                                ) {
                                    AsyncImage(
                                        modifier = Modifier.fillMaxSize(),
                                        model = ImageRequest.Builder(
                                            LocalPlatformContext.current
                                        ).data(screenState.thumbnail)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "Product thumbnail Image",
                                        contentScale = ContentScale.Crop
                                    )
                                    Box(
                                        modifier = Modifier
                                            .padding(
                                                top = 12.dp,
                                                end = 12.dp
                                            )
                                            .clip(
                                                shape = RoundedCornerShape(size = 6.dp)
                                            )
                                            .background(ButtonPrimary)
                                            .clickable {
                                                viewModel.deleteThumbnailFromStorage(
                                                    onSuccess = {
                                                        messageBarState.addSuccess("Thumbnail deleted successfully")
                                                    },
                                                    onError = { error ->
                                                        messageBarState.addError(error)
                                                    }
                                                )
                                            }
                                            .padding(12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            modifier = Modifier.size(14.dp),
                                            painter = painterResource(Resources.Icon.Delete),
                                            contentDescription = "Delete icon"
                                        )
                                    }
                                }
                            },
                            onError = { message ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    ErrorCard(
                                        message = message
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    TextButton(
                                        onClick = {
                                            viewModel.updateThumbnailUploaderState(
                                                RequestState.Idle
                                            )
                                        },
                                        colors = ButtonDefaults.textButtonColors(
                                            containerColor = Color.Transparent,
                                            contentColor = TextSecondary
                                        ),
                                        content = {
                                            Text(
                                                text = "Try Again",
                                                fontSize = FontSize.SMALL,
                                                color = TextSecondary
                                            )
                                        }
                                    )
                                }
                            }
                        )
                    }
                    CustomTextField(
                        value = screenState.title,
                        onValueChange = viewModel::updateTitle,
                        placeholder = "Title"
                    )
                    CustomTextField(
                        modifier = Modifier.height(168.dp),
                        value = screenState.description,
                        onValueChange = viewModel::updateDescription,
                        placeholder = "Description",
                        expanded = true
                    )
                    AlertTextField(
                        modifier = Modifier.fillMaxWidth(),
                        text = screenState.category.title,
                        onClick = {
                            showCategoriesShow = true
                        }
                    )
                    CustomTextField(
                        value = screenState.weight?.toString().orEmpty(),
                        onValueChange = { text ->
                            viewModel.updateWeight(text.toIntOrNull() ?: 0)
                        },
                        placeholder = "Weight (Optional)",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                    CustomTextField(
                        value = screenState.flavours,
                        onValueChange = viewModel::updateFlavours,
                        placeholder = "Flavours (Optional)"
                    )
                    CustomTextField(
                        value = screenState.price.toString(),
                        onValueChange = { text ->
                            viewModel.updatePrice(text.toDoubleOrNull() ?: 0.0)
                        },
                        placeholder = "Price",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = if (id == null) "Add new product" else "Update",
                    icon = if (id == null) Resources.Icon.Plus else Resources.Icon.Checkmark,
                    enabled = isFormValid,
                    onClick = {
                        viewModel.createProduct(
                            onSuccess = {
                                messageBarState.addSuccess("Product successfully added!")
                            },
                            onError = { message ->
                                messageBarState.addError(message)
                            })
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun ManageProductScreenPreview() {
    ManageProductScreen(
        id = null
    ) {

    }
}
