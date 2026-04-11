package com.nutrisport.categories.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nutrisport.shared.BebasNeueFont
import com.nutrisport.shared.BorderIdle
import com.nutrisport.shared.FontSize
import com.nutrisport.shared.SurfaceLighter
import com.nutrisport.shared.TextPrimary
import com.nutrisport.shared.domain.ProductCategory

@Composable
fun CategoryCard(
    modifier: Modifier = Modifier,
    productCategory: ProductCategory,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(size = 12.dp))
            .background(color = SurfaceLighter)
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = BorderIdle,
                shape = RoundedCornerShape(size = 12.dp)
            )
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 16.dp,
                vertical = 20.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(12.dp)
                .background(color = productCategory.color)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = productCategory.title.uppercase(),
            fontSize = FontSize.EXTRA_MEDIUM,
            color = TextPrimary,
            fontFamily = BebasNeueFont(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}