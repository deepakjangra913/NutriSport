package com.nutrisport.shared.component.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.nutrisport.shared.IconWhite
import com.nutrisport.shared.Resources
import com.nutrisport.shared.SurfaceLighter
import com.nutrisport.shared.SurfaceSecondary
import com.nutrisport.shared.domain.Country
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CountryPickerDialog() {

}

@Composable
fun CountryCode(
    country: Country,
    isSelected: Boolean,
    onSelect: () -> Unit
){

}

@Composable
private fun Selector(
    modifier: Modifier = Modifier,
    isSelected: Boolean
) {

    val animatedBackground by animateColorAsState(
        if (isSelected) SurfaceSecondary else SurfaceLighter
    )

    Box(
        modifier = modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(animatedBackground),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            isSelected
        ) {
            Icon(
                modifier = Modifier.size(14.dp),
                painter = painterResource(Resources.Icon.Checkmark),
                contentDescription = "Checkmark Icon",
                tint = IconWhite
            )
        }

    }
}

@Preview
@Composable
fun SelectorPreview() {
    Selector(
        isSelected = true
    )
}