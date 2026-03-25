package com.nutrisport.auth.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.nutrisport.shared.BorderIdle
import com.nutrisport.shared.FontSize.REGULAR
import com.nutrisport.shared.IconSecondary
import com.nutrisport.shared.Resources
import com.nutrisport.shared.SurfaceLighter
import com.nutrisport.shared.TextPrimary
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * A customizable Google Sign-In button with built-in loading state handling.
 *
 * This composable displays a button styled for Google authentication, showing
 * an icon and text by default, and switching to a loading indicator with
 * alternate text when [loading] is true.
 *
 * Features:
 * - Displays Google logo with primary text in idle state.
 * - Shows a circular progress indicator with secondary text during loading.
 * - Disables clicks while loading to prevent multiple triggers.
 * - Smoothly animates content changes using [AnimatedContent] and [animateContentSize].
 * - Fully customizable appearance including shape, colors, and text.
 *
 * Behavior:
 * - When [loading] is `true`, the button:
 *   - Replaces the icon with a progress indicator.
 *   - Updates the displayed text to [secondaryText].
 *   - Disables click interactions.
 * - When [loading] is `false`, the button:
 *   - Displays the Google icon and [primaryText].
 *   - Enables click interactions.
 *
 * @param modifier Modifier to be applied to the button container.
 * @param loading Controls the loading state of the button.
 * @param primaryText Text displayed when the button is idle.
 * @param secondaryText Text displayed when the button is in loading state.
 * @param icon Drawable resource representing the Google logo.
 * @param shape Shape of the button.
 * @param backgroundColor Background color of the button.
 * @param borderColor Border color of the button.
 * @param progressIndicatorColor Color of the loading indicator.
 * @param onClick Callback invoked when the button is clicked (only when not loading).
 *
 * @see AnimatedContent
 * @see CircularProgressIndicator
 */
@Composable
fun GoogleButton(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    primaryText: String = "Sign in with Google",
    secondaryText: String = "Please wait...",
    icon: DrawableResource = Resources.Image.GoogleLogo,
    shape: Shape = RoundedCornerShape(size = 99.dp),
    backgroundColor: Color = SurfaceLighter,
    borderColor: Color = BorderIdle,
    progressIndicatorColor: Color = IconSecondary,
    onClick: () -> Unit
) {

    var buttonText by remember { mutableStateOf(primaryText) }

    LaunchedEffect(loading) {
        buttonText = if (loading) secondaryText else primaryText
    }

    Surface(
        modifier = modifier
            .clip(shape)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = shape
            )
            .clickable(enabled = loading.not()) {
                onClick()
            },
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(all = 20.dp)
                .animateContentSize(
                    animationSpec = tween(durationMillis = 200)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AnimatedContent(
                targetState = loading
            ) { loadingState ->
                if (!loadingState) {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = "Google Logo",
                        tint = Color.Unspecified
                    )
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = progressIndicatorColor
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = buttonText,
                color = TextPrimary,
                fontSize = REGULAR
            )
        }
    }
}