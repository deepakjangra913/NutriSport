package com.nutrisport.products_overview.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.nutrisport.shared.Alpha
import com.nutrisport.shared.FontSize
import com.nutrisport.shared.IconWhite
import com.nutrisport.shared.Resources
import com.nutrisport.shared.RobotoCondensedFont
import com.nutrisport.shared.TextBrand
import com.nutrisport.shared.TextWhite
import com.nutrisport.shared.domain.Product
import com.nutrisport.shared.domain.ProductCategory
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MainProductCard(
    product: Product,
    isVisible: Boolean = false,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    val infiniteTransition = rememberInfiniteTransition()
    val animateScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val animatedRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(size = 12.dp))
            .clickable { onClick(product.id) }
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .animateContentSize()
                .then(
                    if (isVisible) Modifier
                        .scale(animateScale)
                        .rotate(animatedRotation)
                    else Modifier
                ),
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(product.thumbnail)
                .crossfade(enable = true)
                .build(),
            contentDescription = "Product thumbnail",
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black,
                            Color.Black.copy(alpha = Alpha.ZERO)
                        ),
                        startY = Float.POSITIVE_INFINITY,
                        endY = 0.0f
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 12.dp),
            verticalArrangement = Arrangement.Bottom
        ) {

            Text(
                text = product.title,
                fontSize = FontSize.EXTRA_MEDIUM,
                color = TextWhite,
                fontFamily = RobotoCondensedFont(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = product.description,
                fontSize = FontSize.REGULAR,
                color = TextWhite.copy(alpha = Alpha.HALF),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                lineHeight = FontSize.REGULAR * 1.3f
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                AnimatedContent(
                    targetState = product.category
                ) { category ->
                    if (ProductCategory.valueOf(category) == ProductCategory.Accessories) {
                        Spacer(Modifier.weight(1f))
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.size(14.dp),
                                painter = painterResource(Resources.Icon.Weight),
                                contentDescription = "Weight icon",
                                tint = IconWhite
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = "${product.weight}g",
                                fontSize = FontSize.EXTRA_SMALL,
                                color = TextWhite,
                            )
                        }
                    }
                }

                Text(
                    text = "$${product.price}",
                    fontSize = FontSize.EXTRA_REGULAR,
                    fontWeight = FontWeight.Medium,
                    color = TextBrand,
                )
            }
        }
    }
}

@Preview
@Composable
fun MainProductCardPreview() {
    MainProductCard(
        product = Product(
            id = "",
            title = "Title",
            createdAt = 838893,
            description = "Description",
            thumbnail = "",
            category = "Protein",
            flavours = listOf("Chocolate"),
            weight = 4000,
            price = 100.00,
            isPopular = false,
            isDiscounted = false,
            isNew = false
        ),
        isVisible = false,
        onClick = {

        }
    )
}