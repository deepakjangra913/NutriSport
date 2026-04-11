package com.nutrisport.category_search

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nutrisport.shared.BebasNeueFont
import com.nutrisport.shared.BorderIdle
import com.nutrisport.shared.FontSize
import com.nutrisport.shared.IconPrimary
import com.nutrisport.shared.Resources
import com.nutrisport.shared.Surface
import com.nutrisport.shared.SurfaceLighter
import com.nutrisport.shared.TextPrimary
import com.nutrisport.shared.component.InfoCard
import com.nutrisport.shared.component.LoadingCard
import com.nutrisport.shared.component.ProductCard
import com.nutrisport.shared.domain.ProductCategory
import com.nutrisport.shared.util.DisplayResult
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySearchScreen(
    navigateBack: () -> Unit,
    navigateToDetails: (String) -> Unit,
    category: ProductCategory
) {
    val viewModel = koinViewModel<CategorySearchViewModel>()
    val filterProducts by viewModel.filterProducts.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    var searchBarVisible by mutableStateOf(false)

    Scaffold(
        containerColor = Surface,
        topBar = {
            AnimatedContent(searchBarVisible) { visible ->
                if (visible) {
                    SearchBar(
                        modifier = Modifier
                            .padding(
                                horizontal = 12.dp
                            ).fillMaxWidth(),
                        colors = SearchBarDefaults.colors(
                            containerColor = SurfaceLighter,
                            dividerColor = BorderIdle
                        ),
                        expanded = false,
                        onExpandedChange = {},
                        inputField = {
                            SearchBarDefaults.InputField(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = SurfaceLighter,
                                    focusedContainerColor = SurfaceLighter
                                ),
                                query = searchQuery,
                                onQueryChange = viewModel::updateSearchQuery,
                                expanded = false,
                                onExpandedChange = {},
                                onSearch = {},
                                placeholder = {
                                    Text(
                                        text = "Search here...",
                                        color = TextPrimary,
                                        fontSize = FontSize.REGULAR
                                    )
                                },
                                trailingIcon = {
                                    IconButton(
                                        modifier = Modifier.size(14.dp),
                                        onClick = {
                                            if (searchQuery.isNotEmpty()) {
                                                viewModel.updateSearchQuery("")
                                            } else {
                                                searchBarVisible = false
                                            }
                                        }) {
                                        Icon(
                                            painter = painterResource(Resources.Icon.Close),
                                            contentDescription = "Close Icon",
                                            tint = IconPrimary
                                        )
                                    }
                                }
                            )
                        },
                        content = {}
                    )
                } else {
                    TopAppBar(
                        title = {
                            Text(
                                text = category.title,
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
                                    contentDescription = "Close Icon",
                                    tint = IconPrimary
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                searchBarVisible = true
                            }) {
                                Icon(
                                    painter = painterResource(Resources.Icon.Search),
                                    contentDescription = "Search Icon",
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
            }
        },
        content = { paddingValues ->
            filterProducts.DisplayResult(
                modifier = Modifier.padding(
                    bottom = paddingValues.calculateBottomPadding(),
                    top = paddingValues.calculateTopPadding()
                ),
                onLoading = {
                    LoadingCard(
                        modifier = Modifier.fillMaxSize()
                    )
                },
                onSuccess = { categoryProducts ->
                    AnimatedContent(
                        targetState = categoryProducts
                    ) { products ->
                        if (products.isNotEmpty()) {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize()
                                    .padding(all = 12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(items = products, key = {
                                    it.id
                                }) { product ->
                                    ProductCard(
                                        product = product,
                                        onClick = { id ->
                                            navigateToDetails(id)
                                        }
                                    )
                                }
                            }
                        } else {
                            InfoCard(
                                icon = Resources.Image.Cat,
                                title = "Nothing here",
                                subTitle = "We couldn't find any product."
                            )
                        }
                    }
                },
                onError = { message ->
                    InfoCard(
                        modifier = Modifier.fillMaxSize(),
                        icon = Resources.Image.Cat,
                        title = "Oops!",
                        subTitle = message
                    )
                },
                transitionSpec = { fadeIn() togetherWith fadeOut() }
            )
        }
    )
}