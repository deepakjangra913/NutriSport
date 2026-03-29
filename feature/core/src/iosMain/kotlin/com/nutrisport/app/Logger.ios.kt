package com.nutrisport.app

actual fun logDebug(tag: String, message: String) {
    println("$tag: $message")
}