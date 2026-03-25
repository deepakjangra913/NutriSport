package org.deepak.nutrisport

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.initialize
import com.nutrisport.di.initializeKoin
import org.koin.android.ext.koin.androidContext

/**
 * Custom [Application] class responsible for initializing app-wide dependencies.
 *
 * This class is invoked before any Activity or Composable is created and is used
 * to set up global configurations such as dependency injection and Firebase.
 *
 * Responsibilities:
 * - Initializes Koin for dependency injection with Android context.
 * - Initializes Firebase for backend services (e.g., authentication, database).
 *
 * Behavior:
 * - Koin is configured with [androidContext] to provide Android-specific dependencies.
 * - Firebase is initialized once for the entire application lifecycle.
 *
 * @see Application
 * @see initializeKoin
 * @see Firebase
 */
class MyApplication : Application() {

    /**
     * Called when the application is starting, before any other components.
     *
     * Initializes dependency injection and Firebase services required across the app.
     */
    override fun onCreate() {
        super.onCreate()

        initializeKoin(
            config = {
                androidContext(this@MyApplication)
            }
        )

        Firebase.initialize(context = this)
    }
}