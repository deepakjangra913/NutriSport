package org.deepak.nutrisport

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.nutrisport.shared.util.PreferencesRepository

/**
 * Main entry point Activity for the Android application.
 *
 * This activity sets up the initial UI using Jetpack Compose and configures
 * system-level behaviors such as splash screen and edge-to-edge rendering.
 *
 * Responsibilities:
 * - Displays the system splash screen using [installSplashScreen].
 * - Enables edge-to-edge layout for immersive UI experience.
 * - Configures transparent status and navigation bars.
 * - Sets the root composable [App] as the activity content.
 *
 * Behavior:
 * - On creation, the splash screen is shown until the app is ready.
 * - System bars are configured with light appearance and transparent backgrounds.
 * - The Compose UI is rendered using [setContent].
 *
 * @see ComponentActivity
 * @see installSplashScreen
 * @see enableEdgeToEdge
 * @see App
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            )
        )
        setContent {
            App()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val uri = intent.data

        val isSuccess = uri?.getQueryParameter("success")
        val isCancelled = uri?.getQueryParameter("cancel")
        val token = uri?.getQueryParameter("token")

        PreferencesRepository.savePayPalData(
            isSuccess = isSuccess?.toBooleanStrictOrNull(),
            error = if (isCancelled == "null") null
            else "Payment has been canceled",
            token = token
        )
    }
}