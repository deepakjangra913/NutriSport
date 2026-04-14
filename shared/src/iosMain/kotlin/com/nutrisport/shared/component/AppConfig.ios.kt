package com.nutrisport.shared.component

import platform.Foundation.NSBundle

actual object AppConfig {
    actual val paypalClientId: String
        get() = NSBundle.mainBundle.objectForInfoDictionaryKey("PaypalClientID") as? String
            ?: error("PAYPAL_CLIENT_ID missing from Info.plist")
    actual val paypalSecret: String
        get() = NSBundle.mainBundle.objectForInfoDictionaryKey("PaypalSecret") as? String
            ?: error("PAYPAL_SECRET missing from Info.plist")
}