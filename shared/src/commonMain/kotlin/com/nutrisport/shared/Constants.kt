package com.nutrisport.shared

import com.nutrisport.shared.component.AppConfig

object Constants {
    const val WEB_CLIENT_ID =
        "771528812551-8qvq0pk8eecfavuvimtp712lt7h1h4mh.apps.googleusercontent.com"

    const val PAYPAL_AUTH_ENDPOINT = "https://api-m.sandbox.paypal.com/v1/oauth2/token"

    val PAYPAL_AUTH_KEY = "${AppConfig.paypalClientId}:${AppConfig.paypalSecret}"
    const val MAX_QUANTITY = 10
    const val MINIMUM_QUANTITY = 1
}