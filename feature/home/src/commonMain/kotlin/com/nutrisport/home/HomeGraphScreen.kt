package com.nutrisport.home

import ContentWithMessageBar
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nutrisport.cart.CartScreen
import com.nutrisport.categories.CategoriesScreen
import com.nutrisport.home.component.BottomBar
import com.nutrisport.home.component.CustomDrawer
import com.nutrisport.home.domain.BottomBarDestination
import com.nutrisport.home.domain.CustomDrawerState
import com.nutrisport.home.domain.isOpened
import com.nutrisport.home.domain.opposite
import com.nutrisport.products_overview.ProductsOverviewScreen
import com.nutrisport.shared.Alpha
import com.nutrisport.shared.BebasNeueFont
import com.nutrisport.shared.FontSize
import com.nutrisport.shared.IconPrimary
import com.nutrisport.shared.Resources
import com.nutrisport.shared.Surface
import com.nutrisport.shared.SurfaceLighter
import com.nutrisport.shared.TextPrimary
import com.nutrisport.shared.navigation.Screen
import com.nutrisport.shared.util.RequestState
import com.nutrisport.shared.util.getScreenWidth
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeGraphScreen(
    navigateToAuth: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToAdminPanel: () -> Unit,
    navigateToDetails: (String) -> Unit,
    navigateToCategorySearch: (String) -> Unit,
    navigateToCheckoutScreen: (String) -> Unit
) {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState()
    val viewModel = koinViewModel<HomeGraphViewModel>()
    val customer by viewModel.customerData.collectAsStateWithLifecycle()
    val totalAmount by viewModel.totalAmountFlow.collectAsStateWithLifecycle(RequestState.Loading)

    val selectedDestination by remember {
        derivedStateOf {
            val route = currentRoute.value?.destination?.route.toString()
            when {
                route.contains(BottomBarDestination.ProductOverview.screen.toString()) -> {
                    BottomBarDestination.ProductOverview
                }

                route.contains(BottomBarDestination.Cart.screen.toString()) -> {
                    BottomBarDestination.Cart
                }

                route.contains(BottomBarDestination.Categories.screen.toString()) -> {
                    BottomBarDestination.Categories
                }

                else -> {
                    BottomBarDestination.ProductOverview
                }
            }
        }
    }

    val screenWith = remember { getScreenWidth() }
    var drawerState by remember { mutableStateOf(CustomDrawerState.Closed) }
    val offsetValue by remember {
        derivedStateOf {
            (screenWith / 1.5).dp
        }
    }
    val animatedOffset by animateDpAsState(
        if (drawerState.isOpened()) offsetValue else 0.dp
    )

    val animatedBackground by animateColorAsState(
        if (drawerState.isOpened()) SurfaceLighter else Surface
    )

    val animatedScale by animateFloatAsState(
        targetValue = if (drawerState.isOpened()) .9f else 1f
    )

    val animatedRadius by animateDpAsState(
        targetValue = if (drawerState.isOpened()) 12.dp else 0.dp
    )

    val messageBarState = rememberMessageBarState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(animatedBackground)
    ) {
        CustomDrawer(
            customer,
            onProfileClick = {
                navigateToProfile()
            },
            onBlogClick = {},
            onLocationsClick = {},
            onContactUsClick = {},
            onSignOutClick = {
                viewModel.signOut(
                    onSuccess = navigateToAuth,
                    onError = { errorMessage ->
                        messageBarState.addError(
                            errorMessage
                        )
                    }
                )
            },
            onAdminPanelClick = {
                navigateToAdminPanel()
            }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(size = animatedRadius))
                .offset(x = animatedOffset)
                .scale(scale = animatedScale)
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(animatedRadius),
                    ambientColor = Color.Black.copy(alpha = Alpha.DISABLED),
                    spotColor = Color.Black.copy(alpha = Alpha.DISABLED)
                )
        ) {
            Scaffold(
                containerColor = Surface,
                topBar = {
                    CenterAlignedTopAppBar(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        title = {
                            AnimatedContent(
                                targetState = selectedDestination
                            ) { destination ->
                                Text(
                                    text = destination.title,
                                    fontSize = FontSize.LARGE,
                                    fontFamily = BebasNeueFont(),
                                    color = TextPrimary
                                )
                            }
                        },
                        actions = {
                            AnimatedVisibility(
                                visible = selectedDestination == BottomBarDestination.Cart
                            ) {
                                IconButton(
                                    onClick = {
                                        if (totalAmount.isSuccess()) {
                                            navigateToCheckoutScreen(
                                                totalAmount.getSuccessData().toString()
                                            )
                                        } else if (totalAmount.isError()) {
                                            messageBarState.addError("Error while calculating a total amount: ${totalAmount.getErrorMessage()}")
                                        }
                                    }
                                ) {
                                    if (customer.isSuccess() && customer.getSuccessData().cart.isNotEmpty()) {
                                        Icon(
                                            painter = painterResource(Resources.Icon.RightArrow),
                                            contentDescription = "Right Arrow",
                                            tint = IconPrimary
                                        )
                                    }
                                }
                            }

                        },
                        navigationIcon = {
                            AnimatedContent(
                                targetState = drawerState
                            ) { drawer ->
                                if (drawer.isOpened()) {
                                    IconButton(
                                        onClick = {
                                            drawerState = drawerState.opposite()
                                        }
                                    ) {
                                        Icon(
                                            painter = painterResource(Resources.Icon.Close),
                                            contentDescription = "Close Icon",
                                            tint = IconPrimary
                                        )
                                    }
                                } else {
                                    IconButton(
                                        onClick = {
                                            drawerState = drawerState.opposite()
                                        }
                                    ) {
                                        Icon(
                                            painter = painterResource(Resources.Icon.Menu),
                                            contentDescription = "Menu Icon",
                                            tint = IconPrimary
                                        )
                                    }
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Surface,
                            scrolledContainerColor = Surface,
                            navigationIconContentColor = IconPrimary,
                            titleContentColor = TextPrimary,
                            actionIconContentColor = IconPrimary
                        )
                    )
                }
            ) { paddingValues ->
                ContentWithMessageBar(
                    modifier = Modifier.fillMaxSize()
                        .padding(
                            top = paddingValues.calculateTopPadding(),
                            bottom = paddingValues.calculateBottomPadding()
                        ),
                    messageBarState = messageBarState,
                    errorMaxLines = 2,
                    contentBackgroundColor = Surface
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        NavHost(
                            modifier = Modifier.weight(1f),
                            navController = navController,
                            startDestination = Screen.ProductOverview
                        ) {
                            composable<Screen.ProductOverview> {
                                ProductsOverviewScreen(navigateToDetails = navigateToDetails)
                            }
                            composable<Screen.Cart> {
                                CartScreen()
                            }
                            composable<Screen.Categories> {
                                CategoriesScreen(
                                    navigateToCategoriesSearch = navigateToCategorySearch
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier.padding(all = 12.dp)
                        ) {
                            BottomBar(
                                selected = selectedDestination,
                                customer = customer,
                                onSelect = { destination ->
                                    navController.navigate(
                                        destination.screen
                                    ) {
                                        launchSingleTop = true
                                        popUpTo<Screen.ProductOverview> {
                                            saveState = true
                                            inclusive = false
                                        }
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
