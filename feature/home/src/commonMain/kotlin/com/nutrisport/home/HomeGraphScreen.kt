package com.nutrisport.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nutrisport.home.component.CustomDrawer
import com.nutrisport.home.domain.BottomBarDestination
import com.nutrisport.shared.SurfaceLighter
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomeGraphScreen() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState()

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceLighter)
            .systemBarsPadding()
    ) {
        CustomDrawer(
            onProfileClick = {},
            onBlogClick = {},
            onLocationsClick = {},
            onContactUsClick = {},
            onSignOutClick = {},
            onAdminPanelClick = {}
        )
        /*Scaffold(
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
                    navigationIcon = {
                        Icon(
                            painter = painterResource(Resources.Icon.Menu),
                            contentDescription = "Menu Icon",
                            tint = IconPrimary
                        )
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
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        bottom = paddingValues.calculateBottomPadding()
                    )
            ) {
                NavHost(
                    modifier = Modifier.weight(1f),
                    navController = navController,
                    startDestination = Screen.ProductOverview
                ) {
                    composable<Screen.ProductOverview> {

                    }
                    composable<Screen.Cart> {

                    }
                    composable<Screen.Categories> {

                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier.padding(all = 12.dp)
                ) {
                    BottomBar(
                        selected = selectedDestination,
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
        }*/
    }
}
