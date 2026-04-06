package com.nutrisport.di

import com.nutrisport.admin_panel.AdminPanelViewModel
import com.nutrisport.auth.AuthViewModel
import com.nutrisport.data.AdminRepositoryImpl
import com.nutrisport.data.CustomRepositoryImpl
import com.nutrisport.data.ProductsRepositoryImpl
import com.nutrisport.data.domain.AdminRepository
import com.nutrisport.data.domain.CustomerRepository
import com.nutrisport.data.domain.ProductsRepository
import com.nutrisport.details.DetailsViewModel
import com.nutrisport.home.HomeGraphViewModel
import com.nutrisport.manage_product.ManageProductViewModel
import com.nutrisport.products_overview.ProductsOverviewViewModel
import com.nutrisport.profile.ProfileViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Shared Koin module containing common dependency definitions used across platforms.
 *
 * This module registers:
 * - Repository implementations for [CustomerRepository] and [AdminRepository]
 * - Shared ViewModels used in the application
 *
 * These dependencies are available for all targets unless overridden by a platform-specific module.
 */
val sharedModule = module {
    single<CustomerRepository> { CustomRepositoryImpl() }
    single<AdminRepository> { AdminRepositoryImpl() }
    single<ProductsRepository> { ProductsRepositoryImpl() }
    viewModelOf(::AuthViewModel)
    viewModelOf(::HomeGraphViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::ManageProductViewModel)
    viewModelOf(::AdminPanelViewModel)
    viewModelOf(::ProductsOverviewViewModel)
    viewModelOf(::ProductsOverviewViewModel)
    viewModelOf(::DetailsViewModel)
}

/**
 * Platform-specific Koin module expected from each target source set.
 *
 * Each platform such as Android or iOS must provide its own actual implementation
 * of this module to register target-specific dependencies.
 */
expect val targetModule: Module

/**
 * Initializes Koin dependency injection for the application.
 *
 * This function starts Koin with:
 * - The shared module containing common dependencies
 * - The platform-specific [targetModule]
 * - An optional [config] block for additional Koin configuration
 *
 * The [config] parameter can be used to add extra setup such as logging,
 * properties, or additional modules during initialization.
 *
 * Example:
 * ```kotlin
 * initializeKoin {
 *     printLogger()
 * }
 * ```
 *
 * @param config Optional Koin application configuration block.
 */
fun initializeKoin(
    config: (KoinApplication.() -> Unit)? = null
) {
    startKoin {
        config?.invoke(this)
        modules(sharedModule, targetModule)
    }
}