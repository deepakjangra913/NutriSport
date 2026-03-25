package com.nutrisport.auth

import ContentWithMessageBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mmk.kmpauth.firebase.google.GoogleButtonUiContainerFirebase
import com.nutrisport.auth.component.GoogleButton
import com.nutrisport.shared.Alpha
import com.nutrisport.shared.BebasNeueFont
import com.nutrisport.shared.FontSize
import com.nutrisport.shared.FontSize.EXTRA_REGULAR
import com.nutrisport.shared.Surface
import com.nutrisport.shared.SurfaceBrand
import com.nutrisport.shared.SurfaceError
import com.nutrisport.shared.TextBrand
import com.nutrisport.shared.TextPrimary
import com.nutrisport.shared.TextWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState

/**
 * Authentication screen that handles user sign-in using Google and Firebase.
 *
 * This composable displays a branded sign-in UI along with a Google Sign-In button.
 * Upon successful authentication, it creates a customer entry via [AuthViewModel]
 * and navigates the user to the home screen.
 *
 * Features:
 * - Displays app branding and sign-in prompt.
 * - Integrates Google Sign-In with Firebase authentication.
 * - Shows success and error messages using a message bar.
 * - Handles loading state during authentication.
 * - Performs navigation after successful sign-in with a slight delay.
 *
 * Flow:
 * 1. User clicks on the Google Sign-In button.
 * 2. Authentication result is received via [onResult].
 * 3. On success:
 *    - Calls [AuthViewModel.createCustomer].
 *    - Shows a success message.
 *    - Navigates to home after a delay.
 * 4. On failure:
 *    - Displays appropriate error messages (network, cancellation, or unknown).
 *
 * @param navigateToHome Callback invoked after successful authentication and customer creation.
 *
 * @see AuthViewModel
 * @see GoogleButtonUiContainerFirebase
 */
@Composable
fun AuthScreen(navigateToHome: () -> Unit) {

    val scope = rememberCoroutineScope()
    val viewModel = koinViewModel<AuthViewModel>()
    val messageBarState = rememberMessageBarState()
    var loadingState by remember { mutableStateOf(false) }

    Scaffold { padding ->
        ContentWithMessageBar(
            contentBackgroundColor = Surface,
            modifier = Modifier.padding(padding),
            messageBarState = messageBarState,
            errorMaxLines = 2,
            errorContentColor = TextWhite,
            errorContainerColor = SurfaceError,
            successContainerColor = SurfaceBrand,
            successContentColor = TextPrimary
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "NUTRISPORT",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontFamily = BebasNeueFont(),
                        color = TextBrand,
                        fontSize = FontSize.EXTRA_LARGE
                    )
                    Text(
                        text = "Sign in to continue",
                        modifier = Modifier.fillMaxWidth()
                            .alpha(Alpha.HALF),
                        textAlign = TextAlign.Center,
                        color = TextPrimary,
                        fontSize = EXTRA_REGULAR
                    )
                }

                GoogleButtonUiContainerFirebase(
                    linkAccount = false,
                    onResult = { result ->
                        result.onSuccess { user ->
                            viewModel.createCustomer(
                                user,
                                onSuccess = {
                                    scope.launch {
                                        messageBarState.addSuccess("Authentication successful!")
                                        delay(2000)
                                        navigateToHome()
                                    }
                                },
                                onError = { error ->
                                    messageBarState.addError(error)
                                }
                            )
                            loadingState = false
                        }
                        result.onFailure { error ->
                            if (error.message?.contains("A network error") == true) {
                                messageBarState.addError("Internet connection unavailable.")
                            } else if (error.message?.contains("Idtoken is null") == true) {
                                messageBarState.addError("Sign in canceled.")
                            } else {
                                messageBarState.addError(error.message ?: "Unknown")
                            }
                            loadingState = false
                        }
                    },
                    content = {
                        GoogleButton(loading = loadingState, onClick = {
                            loadingState = true
                            this@GoogleButtonUiContainerFirebase.onClick()
                        })
                    }
                )
            }
        }
    }
}