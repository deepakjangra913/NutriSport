package com.nutrisport.shared.util

/**
 * Returns the width of the current device screen.
 *
 * This is declared as an `expect` function so that each platform
 * (Android, iOS, Desktop, etc.) can provide its own implementation
 * using platform-specific APIs.
 *
 * The returned value represents the screen width in pixels and is
 * typically used for responsive UI adjustments such as adapting layouts,
 * calculating component sizes, or handling device-specific UI behavior.
 *
 * @return The width of the device screen in pixels.
 */
expect fun getScreenWidth(): Float