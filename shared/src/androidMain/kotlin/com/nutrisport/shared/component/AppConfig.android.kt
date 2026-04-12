package com.nutrisport.shared.component

import org.nutrisport.shared.BuildConfig

actual object AppConfig {
    actual val paypalClientId: String
        get() = BuildConfig.PAYPAL_CLIENT_ID
    actual val paypalSecret: String
        get() =  BuildConfig.PAYPAL_SECRET
}