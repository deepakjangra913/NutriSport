package com.nutrisport.shared

/**
 * Contains commonly used alpha (opacity) values for UI components.
 *
 * These constants help maintain consistent transparency levels across
 * the application and avoid the use of magic numbers in the UI layer.
 *
 * Alpha values range from `0f` (fully transparent) to `1f` (fully opaque).
 * They are typically used with Compose modifiers such as `Modifier.alpha()`
 * or when adjusting color opacity.
 */
object Alpha {

    /**
     * Fully opaque element with no transparency.
     */
    const val FULL = 1f

    /**
     * 50% transparency, often used for secondary content or overlays.
     */
    const val HALF = 0.5f

    /**
     * Standard disabled alpha used for inactive UI components
     * following Material design guidelines.
     */
    const val DISABLED = 0.38f

    /**
     * Very subtle transparency used for background effects or dividers.
     */
    const val TEN_PERCENT = 0.1f

    /**
     * Fully transparent element (completely invisible).
     */
    const val ZERO = 0f
}