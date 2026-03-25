package com.nutrisport.manage_product

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.gitlive.firebase.storage.File

/**
 * Android-specific implementation of a photo picker using Activity Result APIs.
 *
 * This class provides functionality to launch the system photo picker and return
 * the selected image as a [File]. It uses Compose state and lifecycle-aware APIs
 * to trigger the picker from the UI layer.
 *
 * Responsibilities:
 * - Triggers the system photo picker when requested.
 * - Handles image selection via Activity Result API.
 * - Converts the selected URI into a [File] abstraction.
 * - Exposes a composable initializer to integrate with Compose UI.
 *
 * Behavior:
 * - Calling [open] updates internal state to request opening the picker.
 * - [InitializePhotoPicker] observes this state and launches the picker.
 * - When an image is selected, [onImageSelect] is invoked with the selected file.
 * - If no image is selected (user cancels), [onImageSelect] is invoked with null.
 *
 * Implementation Details:
 * - Uses [ActivityResultContracts.PickVisualMedia] for image selection.
 * - Uses [rememberLauncherForActivityResult] for lifecycle-safe result handling.
 * - Uses [LaunchedEffect] to react to state changes and trigger picker launch.
 *
 * @see ActivityResultContracts.PickVisualMedia
 * @see rememberLauncherForActivityResult
 * @see LaunchedEffect
 */
@Suppress(names = ["EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING"])
actual class PhotoPicker {

    /**
     * Internal state that controls whether the photo picker should be opened.
     */
    private var openPhotoPicker = mutableStateOf(false)

    /**
     * Triggers the opening of the system photo picker.
     *
     * This updates internal state, which is observed by [InitializePhotoPicker]
     * to launch the picker.
     */
    actual fun open() {
        openPhotoPicker.value = true
    }

    /**
     * Initializes the photo picker within a composable scope.
     *
     * This function must be called from a Composable. It sets up the activity
     * result launcher and listens for state changes to trigger the picker.
     *
     * @param onImageSelect Callback invoked with the selected [File], or null
     * if the user cancels the picker.
     */
    @Composable
    actual fun InitializePhotoPicker(onImageSelect: (File?) -> Unit) {
        val openPickerState by remember { openPhotoPicker }

        val pickMedia = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            uri?.let { onImageSelect(File(it)) } ?: onImageSelect(null)
            openPhotoPicker.value = false
        }

        LaunchedEffect(openPickerState) {
            if (openPickerState) {
                pickMedia.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
        }
    }
}