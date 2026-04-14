package com.nutrisport.shared.navigation

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

/**
 * Represents all navigation destinations used in the application.
 *
 * This sealed hierarchy defines the available screens that can be navigated to
 * within the app. Each object represents a unique navigation route.
 *
 * The class is marked as [Serializable] to allow safe argument passing and
 * state restoration when using navigation frameworks that require serialization.
 *
 * Using a sealed class ensures type-safe navigation and prevents invalid
 * destinations from being referenced throughout the application.
 */
@Serializable
@Stable
sealed class Screen {

    /**
     * Authentication screen where users can sign in or register.
     */
    @Serializable
    data object Auth : Screen()

    /**
     * Root navigation graph for the home section of the application.
     * Typically contains the main user-facing features after authentication.
     */
    @Serializable
    data object HomeGraph : Screen()

    /**
     * Displays the detailed overview of a selected product,
     * including information such as description, price, and images.
     */
    @Serializable
    data object ProductOverview : Screen()

    /**
     * Shopping cart screen where users can review and manage
     * products added to their cart before checkout.
     */
    @Serializable
    data object Cart : Screen()

    /**
     * Displays a list of product categories allowing users
     * to browse items grouped by category.
     */
    @Serializable
    data object Categories : Screen()

    @Serializable
    data class CategorySearchScreen(
        val category: String
    ) : Screen()

    @Serializable
    data object Profile : Screen()

    @Serializable
    data object AdminPanel : Screen()

    @Serializable
    data class ManageProduct(val id: String? = null) : Screen()

    @Serializable
    data class Details(val id: String) : Screen()

    @Serializable
    data class Checkout(
        val totalAmount: String
    ) : Screen()

    @Serializable
    data class PaymentCompleted(
        val isSuccess: Boolean? = null,
        val error: String? = null,
        val token: String? = null
    ) : Screen()
}