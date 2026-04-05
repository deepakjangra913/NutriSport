package com.nutrisport.admin_panel

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import com.nutrisport.shared.ButtonPrimary
import com.nutrisport.shared.FontSize
import com.nutrisport.shared.IconPrimary
import com.nutrisport.shared.Resources
import com.nutrisport.shared.Surface
import com.nutrisport.shared.SurfaceLighter
import com.nutrisport.shared.TextPrimary
import com.nutrisport.shared.component.InfoCard
import com.nutrisport.shared.component.LoadingCard
import com.nutrisport.shared.component.ProductCard
import com.nutrisport.shared.util.DisplayResult
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    navigateBack: () -> Unit,
    navigateToManageProduct: (String?) -> Unit
) {

    val viewModel = koinViewModel<AdminPanelViewModel>()
    val products by viewModel.filteredProducts.collectAsStateWithLifecycle()
    var searchBarVisible by mutableStateOf(false)
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

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
                                onQueryChange = viewModel::updatedSearchQuery,
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
                                            if (searchQuery.isNotEmpty()){
                                                viewModel.updatedSearchQuery("")
                                            }else{
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
                                text = "Admin Panel",
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigateToManageProduct(null)
                },
                contentColor = IconPrimary,
                containerColor = ButtonPrimary,
                content = {
                    Icon(
                        painter = painterResource(Resources.Icon.Plus),
                        contentDescription = "Add icon"
                    )
                }
            )
        }
    ) { paddingValues ->
        products.DisplayResult(
            modifier = Modifier.padding(paddingValues),
            onLoading = {
                LoadingCard(
                    modifier = Modifier.fillMaxWidth()
                )
            },
            onSuccess = { lastProducts ->
                AnimatedContent(targetState = lastProducts) {
                    if (lastProducts.isNotEmpty()) {
                        LazyColumn(
                            state = rememberLazyListState(),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    all = 12.dp
                                ),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = lastProducts,
                                key = { it.id })
                            { product ->
                                ProductCard(
                                    product = product,
                                    onClick = {
                                        navigateToManageProduct(product.id)
                                    }
                                )
                            }
                        }
                    } else {
                        InfoCard(
                            modifier = Modifier.fillMaxSize(),
                            icon = Resources.Image.Cat,
                            title = "Oops!",
                            subTitle = "Products not found"
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
            }
        )
    }
}