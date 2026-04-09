package com.nutrisport.cart

import ContentWithMessageBar
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nutrisport.cart.component.CartItemCard
import com.nutrisport.shared.Resources
import com.nutrisport.shared.Surface
import com.nutrisport.shared.component.InfoCard
import com.nutrisport.shared.util.DisplayResult
import com.nutrisport.shared.util.RequestState
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState

@Composable
fun CartScreen() {
    val messageBarState = rememberMessageBarState()
    val viewModel = koinViewModel<CartViewModel>()
    val cartItemWithProducts by viewModel.cartItemsWithProducts.collectAsStateWithLifecycle(
        RequestState.Loading
    )

    ContentWithMessageBar(
        contentBackgroundColor = Surface,
        modifier = Modifier.fillMaxSize(),
        errorMaxLines = 2,
        messageBarState = messageBarState
    ) {
        cartItemWithProducts.DisplayResult(
            onLoading = {

            },
            onSuccess = { data ->
                if (data.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                            .padding(horizontal = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = data,
                            key = { it.hashCode().toString() }
                        ) { pair ->
                            CartItemCard(
                                cartItem = pair.first,
                                product = pair.second,
                                onMinusClick = { quantity ->
                                    viewModel.updateCartItemQuantity(
                                        pair.first.id,
                                        quantity,
                                        onSuccess = {},
                                        onError = { message ->
                                            messageBarState.addError(message)
                                        }
                                    )
                                },
                                onPlusClick = { quantity ->
                                    viewModel.updateCartItemQuantity(
                                        pair.first.id,
                                        quantity,
                                        onSuccess = {},
                                        onError = { message ->
                                            messageBarState.addError(message)
                                        }
                                    )
                                },
                                onDeleteClick = {}
                            )
                        }
                    }
                } else {
                    InfoCard(
                        icon = Resources.Image.ShoppingCart,
                        title = "Empty Cart",
                        subTitle = "Check some of our products"
                    )
                }
            },
            onError = { message ->
                InfoCard(
                    icon = Resources.Image.Cat,
                    title = "Oops!",
                    subTitle = message
                )
            },
            transitionSpec = { fadeIn() togetherWith fadeOut() }
        )
    }
}