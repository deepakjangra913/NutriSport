package com.nutrisport.payment_completed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nutrisport.shared.Resources
import com.nutrisport.shared.Surface
import com.nutrisport.shared.component.InfoCard
import com.nutrisport.shared.component.PrimaryButton
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PaymentCompletedScreen(
    isSuccess: Boolean?,
    error: String?,
    navigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Surface)
            .systemBarsPadding()
            .padding(all = 24.dp)
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            InfoCard(
                title = if (isSuccess != null) "Success!" else "Oops!",
                subTitle = if (isSuccess != null) "Your purchase is on the way." else error
                    ?: "Unknown error.",
                icon = if (isSuccess != null) Resources.Image.Checkmark else Resources.Image.Cat
            )
        }
        PrimaryButton(
            text = "Go Back",
            icon = Resources.Icon.RightArrow,
            onClick = navigateBack
        )
    }
}

@Preview
@Composable
fun PaymentCompletedScreenPreview() {
    PaymentCompletedScreen(
        isSuccess = true,
        error = ""
    ){

    }
}