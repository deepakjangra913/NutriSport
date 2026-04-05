package com.nutrisport.products_overview

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nutrisport.shared.Alpha
import com.nutrisport.shared.FontSize
import com.nutrisport.shared.Resources
import com.nutrisport.shared.TextPrimary
import com.nutrisport.shared.component.InfoCard
import com.nutrisport.shared.component.LoadingCard
import com.nutrisport.shared.component.ProductCard
import com.nutrisport.shared.util.DisplayResult
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProductsOverviewScreen() {

    val viewModel = koinViewModel<ProductsOverviewViewModel>()
    val products = viewModel.discountedProducts.collectAsStateWithLifecycle()


    products.value.DisplayResult(
        onLoading = {
            LoadingCard(
                modifier = Modifier.fillMaxSize()
            )
        },
        onSuccess = { productsList ->
            AnimatedContent(targetState = productsList) {
                if (productsList.isNotEmpty()) {
                    Column {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .alpha(Alpha.HALF),
                            text = "Discounted Products",
                            fontSize = FontSize.EXTRA_REGULAR,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.padding(horizontal = 12.dp)
                        ) {
                            items(
                                items = productsList.sortedBy { it.createdAt }.take(3),
                                key = { it.id }) { product ->
                                ProductCard(
                                    product = product,
                                    onClick = {}
                                )
                            }
                        }
                    }
                } else {
                    InfoCard(
                        icon = Resources.Image.Cat,
                        title = "Nothing here",
                        subTitle = "Empty product list."
                    )
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