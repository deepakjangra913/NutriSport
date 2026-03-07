package com.nutrisport.di

import com.nutrisport.auth.AuthViewModel
import com.nutrisport.data.CustomRepositoryImpl
import com.nutrisport.data.domain.CustomerRepository
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {
    single<CustomerRepository> { CustomRepositoryImpl() }
    viewModelOf(::AuthViewModel)
}

fun initializeKoin(
    config: (KoinApplication.()-> Unit)? = null
){

    startKoin {
        config?.invoke(this)
        modules(sharedModule)
    }
}