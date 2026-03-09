package com.nutrisport.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import nutrisport.shared.generated.resources.Res
import nutrisport.shared.generated.resources.bebas_neue_regular
import nutrisport.shared.generated.resources.roboto_condensed_medium
import org.jetbrains.compose.resources.Font

/**
 * Provides the Bebas Neue font family used across the application.
 *
 * This font is typically used for branding elements such as
 * titles, headers, and logo text where a bold and distinctive
 * appearance is desired.
 *
 * @return [FontFamily] instance representing the Bebas Neue font.
 */
@Composable
fun BebasNeueFont() = FontFamily(
    Font(Res.font.bebas_neue_regular)
)

/**
 * Provides the Roboto Condensed font family used for standard UI text.
 *
 * This font is optimized for readability and is commonly used for
 * body text, labels, and other content elements within the application.
 *
 * The function is marked as [Composable] because font loading may rely
 * on Compose runtime resources depending on the platform implementation.
 *
 * @return [FontFamily] instance representing the Roboto Condensed font.
 */
@Composable
fun RobotoCondensedFont() = FontFamily(
    Font(Res.font.roboto_condensed_medium)
)

/**
 * Defines the typography scale used throughout the application.
 *
 * Centralizing font sizes helps maintain visual consistency
 * and prevents the use of hardcoded values across the UI layer.
 *
 * These sizes are expressed using Compose's `sp` unit which
 * automatically scales according to user font size preferences.
 */
object FontSize {

    /** Smallest text size typically used for captions or minor labels. */
    val EXTRA_SMALL = 10.sp

    /** Small text size used for secondary labels or supporting text. */
    val SMALL = 12.sp

    /** Default body text size used across most UI elements. */
    val REGULAR = 14.sp

    /** Slightly larger than regular body text for improved readability. */
    val EXTRA_REGULAR = 16.sp

    /** Medium text size used for subtitles or emphasized content. */
    val MEDIUM = 18.sp

    /** Larger medium size often used for section headers. */
    val EXTRA_MEDIUM = 20.sp

    /** Large text size typically used for prominent headings. */
    val LARGE = 30.sp

    /** Extra large size used for branding elements or hero titles. */
    val EXTRA_LARGE = 40.sp
}