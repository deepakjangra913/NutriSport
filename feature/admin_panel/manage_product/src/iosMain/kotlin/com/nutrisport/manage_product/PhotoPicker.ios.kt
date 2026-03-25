package com.nutrisport.manage_product

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import dev.gitlive.firebase.storage.File
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerImageURL
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationController
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UITabBarController
import platform.UIKit.UIViewController
import platform.darwin.NSObject

/**
 * iOS-specific implementation of a photo picker using [UIImagePickerController].
 *
 * This class allows users to select images from the device photo library and returns
 * the selected image as a [File]. It integrates with Jetpack Compose using state and
 * side-effects to trigger the picker presentation.
 *
 * Responsibilities:
 * - Triggers the iOS photo library picker when requested.
 * - Presents [UIImagePickerController] from the top-most view controller.
 * - Handles image selection and cancellation via delegate callbacks.
 * - Converts the selected media URL into a [File] abstraction.
 *
 * Behavior:
 * - Calling [open] updates internal state to request picker launch.
 * - [InitializePhotoPicker] observes this state using [LaunchedEffect].
 * - When triggered:
 *   - Finds the current visible [UIViewController].
 *   - Presents [UIImagePickerController] with image-only media types.
 * - On image selection:
 *   - Invokes [onImageSelect] with the selected file.
 * - On cancellation:
 *   - Invokes [onImageSelect] with null.
 * - Resets internal state after selection to prevent repeated launches.
 *
 * Implementation Details:
 * - Uses [mutableStateOf] to control picker launch state.
 * - Uses [LaunchedEffect] to react to state changes.
 * - Traverses UIKit hierarchy to find the top-most visible view controller.
 * - Uses a custom delegate ([PickerDelegate]) to handle picker callbacks.
 *
 * @see UIImagePickerController
 * @see UIViewController
 * @see LaunchedEffect
 */
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class PhotoPicker {

    /**
     * Internal state controlling whether the photo picker should be opened.
     */
    private var openPhotoPicker = mutableStateOf(false)

    /**
     * Triggers the opening of the iOS photo picker.
     *
     * Updates internal state which is observed by [InitializePhotoPicker]
     * to present the picker.
     */
    actual fun open() {
        openPhotoPicker.value = true
    }

    /**
     * Initializes and manages the lifecycle of the iOS photo picker.
     *
     * Must be called from a Composable. Observes internal state and presents
     * the photo picker when required.
     *
     * @param onImageSelect Callback invoked with the selected [File], or null
     * if the user cancels the picker.
     */
    @Composable
    actual fun InitializePhotoPicker(
        onImageSelect: (File?) -> Unit
    ) {
        val openPhotoPickerState by remember { openPhotoPicker }

        LaunchedEffect(openPhotoPickerState) {
            if (openPhotoPickerState) {
                val viewController = getCurrentViewController()
                val picker = UIImagePickerController().apply {
                    sourceType =
                        UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
                    mediaTypes = listOf("public.image", "public.heif")
                    delegate = PickerDelegate(
                        callback = { file ->
                            onImageSelect(file)
                            openPhotoPicker.value = false
                        }
                    )
                }

                viewController?.presentViewController(
                    picker,
                    animated = true,
                    completion = null
                )
            }
        }
    }

    /**
     * Retrieves the currently visible [UIViewController] to present the picker.
     *
     * @return The top-most visible view controller, or null if unavailable.
     */
    private fun getCurrentViewController(): UIViewController? {
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        return findTopViewController(rootViewController)
    }

    /**
     * Recursively finds the top-most presented view controller.
     *
     * Handles navigation controllers, tab bar controllers, and presented view controllers.
     *
     * @param viewController The starting view controller.
     * @return The top-most visible view controller.
     */
    private fun findTopViewController(viewController: UIViewController?): UIViewController? {
        return when (viewController) {
            is UINavigationController -> findTopViewController(viewController.visibleViewController)
            is UITabBarController -> findTopViewController(viewController.selectedViewController)
            is UIViewController -> {
                viewController.presentedViewController?.let { findTopViewController(it) }
                    ?: viewController
            }
            else -> viewController
        }
    }

    /**
     * Delegate responsible for handling photo picker callbacks.
     *
     * Converts selected media into [File] and propagates results via callback.
     */
    private class PickerDelegate(
        private val callback: (File?) -> Unit
    ) : NSObject(),
        UIImagePickerControllerDelegateProtocol,
        UINavigationControllerDelegateProtocol {

        /**
         * Called when the user selects an image.
         *
         * @param picker The image picker controller.
         * @param didFinishPickingMediaWithInfo Metadata containing selected media info.
         */
        override fun imagePickerController(
            picker: UIImagePickerController,
            didFinishPickingMediaWithInfo: Map<Any?, *>
        ) {
            val url = didFinishPickingMediaWithInfo[UIImagePickerControllerImageURL] as? NSURL
            if (url != null) callback(File(url))
            else callback(null)

            picker.dismissViewControllerAnimated(true, completion = null)
        }

        /**
         * Called when the user cancels the picker.
         *
         * @param picker The image picker controller.
         */
        override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
            callback(null)
            picker.dismissViewControllerAnimated(true, completion = null)
        }
    }
}