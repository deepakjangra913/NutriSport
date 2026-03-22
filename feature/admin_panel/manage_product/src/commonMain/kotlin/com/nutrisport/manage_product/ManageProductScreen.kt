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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nutrisport.shared.BebasNeueFont
import com.nutrisport.shared.BorderIdle
import com.nutrisport.shared.FontSize
import com.nutrisport.shared.IconPrimary
import com.nutrisport.shared.Resources
import com.nutrisport.shared.Surface
import com.nutrisport.shared.SurfaceLighter
import com.nutrisport.shared.TextPrimary
import com.nutrisport.shared.component.AlertTextField
import com.nutrisport.shared.component.CustomTextField
import com.nutrisport.shared.component.PrimaryButton
import com.nutrisport.shared.component.dialog.CategoryPickerDialog
import com.nutrisport.shared.domain.ProductCategory
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState

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
                            .clickable {}
                            .background(color = SurfaceLighter),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(Resources.Icon.Plus),
                            contentDescription = "Image add icon",
                            tint = IconPrimary
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
