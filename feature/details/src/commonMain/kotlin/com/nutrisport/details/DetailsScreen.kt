package com.nutrisport.details

import ContentWithMessageBar
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.nutrisport.details.component.FlavourChip
import com.nutrisport.shared.BebasNeueFont
import com.nutrisport.shared.BorderIdle
import com.nutrisport.shared.FontSize
import com.nutrisport.shared.IconPrimary
import com.nutrisport.shared.Resources
import com.nutrisport.shared.RobotoCondensedFont
import com.nutrisport.shared.Surface
import com.nutrisport.shared.SurfaceLighter
import com.nutrisport.shared.TextPrimary
import com.nutrisport.shared.TextSecondary
import com.nutrisport.shared.component.InfoCard
import com.nutrisport.shared.component.LoadingCard
import com.nutrisport.shared.component.PrimaryButton
import com.nutrisport.shared.component.QuantityCounter
import com.nutrisport.shared.domain.ProductCategory
import com.nutrisport.shared.domain.QuantityCounterSize
import com.nutrisport.shared.util.DisplayResult
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(navigateBack: () -> Unit) {

    val messageBarState = rememberMessageBarState()
    val viewModel = koinViewModel<DetailsViewModel>()
    val product by viewModel.product.collectAsStateWithLifecycle()
    val quantity = viewModel.quantity
    val selectedFlavour = viewModel.selectedFlavour

    Scaffold(
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Details",
                        fontSize = FontSize.LARGE,
                        fontFamily = BebasNeueFont(),
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            painter = painterResource(Resources.Icon.BackArrow),
                            contentDescription = "Close Icon",
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
                actions = {
                    QuantityCounter(
                        size = QuantityCounterSize.Large,
                        value = quantity,
                        onMinusClick = viewModel::updateQuantity,
                        onPlusClick = viewModel::updateQuantity,
                    )
                    Spacer(
                        modifier = Modifier.width(16.dp)
                    )
                }
            )
        },
    ) { padding ->
        product.DisplayResult(
            onLoading = {
                LoadingCard(modifier = Modifier.fillMaxSize())
            },
            onSuccess = { selectedProduct ->
                ContentWithMessageBar(
                    modifier = Modifier
                        .padding(
                            top = padding.calculateTopPadding(),
                            bottom = padding.calculateBottomPadding()
                        ),
                    messageBarState = messageBarState,
                    errorMaxLines = 2,
                    contentBackgroundColor = Surface
                ) {
                    Column {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 24.dp)
                                .padding(top = 12.dp)
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        width = 1.dp,
                                        color = BorderIdle,
                                        shape = RoundedCornerShape(size = 12.dp)
                                    ),
                                model = ImageRequest.Builder(
                                    LocalPlatformContext.current
                                )
                                    .data(selectedProduct.thumbnail)
                                    .crossfade(enable = true)
                                    .build(),
                                contentDescription = "Product Thumbnail Image",
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                AnimatedContent(
                                    targetState = selectedProduct.category
                                ) { category ->
                                    if (ProductCategory.valueOf(category) == ProductCategory.Accessories) {
                                        Spacer(Modifier.weight(1f))
                                    } else {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                modifier = Modifier.size(14.dp),
                                                painter = painterResource(Resources.Icon.Weight),
                                                contentDescription = "Weight icon"
                                            )

                                            Spacer(modifier = Modifier.width(4.dp))

                                            Text(
                                                text = "${selectedProduct.weight}g",
                                                fontSize = FontSize.REGULAR,
                                                color = TextPrimary,
                                            )
                                        }
                                    }
                                }

                                Text(
                                    text = "$${selectedProduct.price}",
                                    fontSize = FontSize.MEDIUM,
                                    fontWeight = FontWeight.Medium,
                                    color = TextSecondary,
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = selectedProduct.title,
                                fontSize = FontSize.EXTRA_MEDIUM,
                                fontWeight = FontWeight.Medium,
                                fontFamily = RobotoCondensedFont(),
                                color = TextPrimary,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = selectedProduct.description,
                                fontSize = FontSize.REGULAR,
                                color = TextPrimary,
                                lineHeight = FontSize.REGULAR * 1.3
                            )

                            Spacer(modifier = Modifier.height(24.dp))
                        }

                        // Bottom CTA
                        Column(
                            modifier = Modifier
                                .background(color = if (selectedProduct.flavours?.isNotEmpty() == true) SurfaceLighter else Surface)
                                .padding(all = 24.dp)
                        ) {
                            if (selectedProduct.flavours?.isNotEmpty() == true) {
                                FlowRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    selectedProduct.flavours?.forEach { flavour ->
                                        FlavourChip(
                                            flavour = flavour,
                                            isSelected = selectedFlavour == flavour,
                                            onClick = {
                                                viewModel.updateFlavour(flavour)
                                            }
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.height(24.dp))
                            }

                            PrimaryButton(
                                icon = Resources.Icon.ShoppingCart,
                                text = "Add to Cart",
                                enabled = if (selectedProduct.flavours?.isNotEmpty() == true) selectedFlavour != null
                                else true,
                                onClick = {
                                    viewModel.addItemToCart(
                                        onSuccess = {
                                            messageBarState.addSuccess("Product added to cart")
                                        },
                                        onError = { message ->
                                            messageBarState.addError(message)
                                        }
                                    )
                                }
                            )
                        }
                    }
                }

            },
            onError = { message ->
                InfoCard(
                    icon = Resources.Image.Cat,
                    title = "Oops!",
                    subTitle = message
                )
            }
        )
    }
}