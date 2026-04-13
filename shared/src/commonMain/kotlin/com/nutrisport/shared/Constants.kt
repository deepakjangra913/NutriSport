package com.nutrisport.shared

import com.nutrisport.shared.component.AppConfig

object Constants {
    const val WEB_CLIENT_ID =
        "771528812551-8qvq0pk8eecfavuvimtp712lt7h1h4mh.apps.googleusercontent.com"

    const val PAYPAL_AUTH_ENDPOINT = "https://api-m.sandbox.paypal.com/v1/oauth2/token"
    const val PAYPAL_CHECKPOINT_ENDPOINT = "https://api-m.sandbox.paypal.com/v2/checkout/orders"

    const val RETURN_URL = "org.deepak.nutrisport://paypalpay?success=true"
    const val CANCEL_URL = "org.deepak.nutrisport://paypalpay?cancel=true"

    val PAYPAL_AUTH_KEY = "${AppConfig.paypalClientId}:${AppConfig.paypalSecret}"

    const val MAX_QUANTITY = 10
    const val MINIMUM_QUANTITY = 1
}