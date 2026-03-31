package com.nutrisport.shared.domain

import androidx.compose.ui.graphics.Color
import com.nutrisport.shared.CategoryBlue
import com.nutrisport.shared.CategoryGreen
import com.nutrisport.shared.CategoryPurple
import com.nutrisport.shared.CategoryRed
import com.nutrisport.shared.CategoryYellow
import kotlinx.serialization.Serializable
import kotlin.time.Clock

/**
 * Represents a product in the catalog.
 *
 * @property id Unique identifier for the product.
 * @property createdAt Timestamp (in milliseconds) when the product was created.
 * Defaults to the current system time.
 * @property title Name/title of the product.
 * @property description Detailed description of the product.
 * @property thumbnail URL or path of the product's thumbnail image.
 * @property category Category to which the product belongs.
 * @property flavours List of available flavours for the product (if applicable).
 * @property weight Weight of the product (if applicable), typically in grams.
 * @property price Price of the product.
 * @property isPopular Indicates whether the product is marked as popular.
 * @property isDiscounted Indicates whether the product is currently discounted.
 * @property isNew Indicates whether the product is newly added.
 */
data class Product(
    val id: String,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val title: String,
    val description: String,
    val thumbnail: String,
    val category: String,
    val flavours: List<String>? = null,
    val weight: Int? = null,
    val price: Double,
    val isPopular: Boolean,
    val isDiscounted: Boolean,
    val isNew: Boolean
)

/**
 * Enum representing different categories of products.
 *
 * @property title Display name of the category.
 * @property color UI color associated with the category.
 */
enum class ProductCategory(
    val title: String,
    val color: Color
) {

    /** Protein supplements category. */
    Protein(
        title = "Protein",
        color = CategoryYellow
    ),

    /** Creatine supplements category. */
    Creatine(
        title = "Creatine",
        color = CategoryBlue
    ),

    /** Pre-workout supplements category. */
    PreWorkout(
        title = "Pre-Workout",
        color = CategoryGreen
    ),

    /** Mass gainers category. */
    Gainers(
        title = "Gainers",
        color = CategoryPurple
    ),

    /** Accessories related to fitness and supplements. */
    Accessories(
        title = "Accessories",
        color = CategoryRed
    )
}
