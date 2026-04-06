package com.nutrisport.products_overview

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nutrisport.products_overview.component.MainProductCard
import com.nutrisport.shared.Alpha
import com.nutrisport.shared.FontSize
import com.nutrisport.shared.Resources
import com.nutrisport.shared.TextPrimary
import com.nutrisport.shared.component.InfoCard
import com.nutrisport.shared.component.LoadingCard
import com.nutrisport.shared.component.ProductCard
import com.nutrisport.shared.util.DisplayResult
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.abs

@Composable
fun ProductsOverviewScreen() {

    val viewModel = koinViewModel<ProductsOverviewViewModel>()
    val products by viewModel.products.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    val centeredIndex: Int? by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
            layoutInfo.visibleItemsInfo.minByOrNull { item ->
                val itemCenter = item.offset + item.size / 2
                abs(itemCenter - viewportCenter)
            }?.index
        }
    }

    products.DisplayResult(
        onLoading = {
            LoadingCard(
                modifier = Modifier.fillMaxSize()
            )
        },
        onSuccess = { productsList ->
            AnimatedContent(targetState = productsList.distinctBy { it.id }) { products ->
                if (products.isNotEmpty()) {
                    Column {
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyRow(
                            state = listState,
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            itemsIndexed(
                                items = products.filter { it.isNew }
                                    .sortedBy { it.createdAt }
                                    .take(6),
                                key = { index, item -> item.id }
                            ) { index, product ->

                                val isLarge = centeredIndex == index
                                val animatedScale by animateFloatAsState(
                                    targetValue = if (isLarge) 1f else 0.8f,
                                    animationSpec = tween(300)
                                )

                                MainProductCard(
                                    modifier = Modifier
                                        .scale(animatedScale)
                                        .height(250.dp)
                                        .fillParentMaxWidth(0.6f),
                                    isVisible = isLarge,
                                    product = product,
                                    onClick = {}
                                )
                            }
                        }
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
                                items = products
                                    .filter { it.isDiscounted }
                                    .sortedBy { it.createdAt }.take(3),
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