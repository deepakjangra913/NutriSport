package org.deepak.nutrisport

import androidx.compose.ui.window.ComposeUIViewController
import com.nutrisport.di.initializeKoin

/**
 * Creates the main iOS view controller for the application using Compose Multiplatform.
 *
 * This function initializes dependency injection via Koin and returns a
 * [ComposeUIViewController] that hosts the root composable [App].
 *
 * Responsibilities:
 * - Initializes Koin for dependency injection before rendering UI.
 * - Bridges Compose Multiplatform UI with the iOS UIKit lifecycle.
 * - Provides the entry point for the shared Compose UI on iOS.
 *
 * Flow:
 * 1. Koin is initialized inside the [configure] block.
 * 2. The root composable [App] is set as the content of the view controller.
 * 3. The returned [ComposeUIViewController] is used by iOS to render the UI.
 *
 * @return A [ComposeUIViewController] hosting the application's root composable.
 *
 * @see ComposeUIViewController
 * @see initializeKoin
 * @see App
 */
fun MainViewController() = ComposeUIViewController(
    configure = {
        initializeKoin()
    }
) { App() }