package com.nutrisport.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.nutrisport.admin_panel.AdminPanelScreen
import com.nutrisport.auth.AuthScreen
import com.nutrisport.category_search.CategorySearchScreen
import com.nutrisport.details.DetailsScreen
import com.nutrisport.home.HomeGraphScreen
import com.nutrisport.manage_product.ManageProductScreen
import com.nutrisport.profile.ProfileScreen
import com.nutrisport.shared.domain.ProductCategory
import com.nutrisport.shared.navigation.Screen

@Composable
fun SetUpNavigationGraph(startDestination: Screen = Screen.Auth) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Screen.Auth> {
            AuthScreen(
                navigateToHome = {
                    navController.navigate(Screen.HomeGraph) {
                        popUpTo<Screen.Auth> {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<Screen.HomeGraph> {
            HomeGraphScreen(
                navigateToAuth = {
                    navController.navigate(Screen.Auth) {
                        popUpTo(Screen.HomeGraph) {
                            inclusive = true
                        }
                    }
                },
                navigateToProfile = {
                    navController.navigate(Screen.Profile) {
                        popUpTo(Screen.HomeGraph) {
                            inclusive = false
                        }
                    }
                },
                navigateToAdminPanel = {
                    navController.navigate(Screen.AdminPanel) {
                        popUpTo(Screen.HomeGraph) {
                            inclusive = false
                        }
                    }
                },
                navigateToDetails = { id ->
                    navController.navigate(Screen.Details(id))
                },
                navigateToCategorySearch = { categoryName ->
                    navController.navigate(Screen.CategorySearchScreen(categoryName))
                }
            )
        }

        composable<Screen.Profile> {
            ProfileScreen(
                onBackPress = {
                    navController.navigateUp()
                }
            )
        }

        composable<Screen.AdminPanel> {
            AdminPanelScreen(
                navigateBack = {
                    navController.navigateUp()
                },
                navigateToManageProduct = { id ->
                    navController.navigate(Screen.ManageProduct(id = id))
                }
            )
        }

        composable<Screen.ManageProduct> {
            val id = it.toRoute<Screen.ManageProduct>().id
            ManageProductScreen(
                id = id,
                navigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable<Screen.Details> {
            DetailsScreen(
                navigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable<Screen.CategorySearchScreen> {
            val category = it.toRoute<Screen.CategorySearchScreen>().category
            val productCategory = ProductCategory.valueOf(category)
            CategorySearchScreen(
                category = productCategory,
                navigateBack = {
                    navController.navigateUp()
                },
                navigateToDetails = { id ->
                    navController.navigate(Screen.Details(id))
                }
            )
        }
    }
}