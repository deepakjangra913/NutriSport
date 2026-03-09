package com.nutrisport.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nutrisport.home.domain.DrawerItem
import com.nutrisport.shared.BebasNeueFont
import com.nutrisport.shared.FontSize
import com.nutrisport.shared.TextBrand
import com.nutrisport.shared.TextPrimary
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CustomDrawer(
    onProfileClick: () -> Unit,
    onBlogClick: () -> Unit,
    onLocationsClick: () -> Unit,
    onContactUsClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onAdminPanelClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxHeight()
            .fillMaxWidth(.6f)
            .padding(horizontal = 12.dp)
    ) {
        Spacer(
            modifier = Modifier.height(50.dp)
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "NUTRISPORT",
            fontFamily = BebasNeueFont(),
            fontSize = FontSize.EXTRA_LARGE,
            color = TextBrand
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Healthy Lifestyle",
            color = TextPrimary,
            textAlign = TextAlign.Center,
            fontSize = FontSize.REGULAR
        )
        Spacer(
            modifier = Modifier.height(50.dp)
        )
        DrawerItem.entries.take(5).forEach { item ->
            DrawerItemCard(
                drawerItem = item,
                onClick = {
                    when (item) {
                        DrawerItem.Profile -> onProfileClick()
                        DrawerItem.Blog -> onBlogClick()
                        DrawerItem.Locations -> onLocationsClick()
                        DrawerItem.ContactUs -> onContactUsClick()
                        DrawerItem.SignOut -> onSignOutClick()
                        DrawerItem.Admin -> {
                            // No need to handle this here
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        Spacer(modifier = Modifier.weight(1f))
        DrawerItemCard(
            drawerItem = DrawerItem.Admin,
            onClick = {
                onAdminPanelClick()
            })
    }
}

@Preview
@Composable
fun CustomDrawerPreview() {
    CustomDrawer(
        onProfileClick = {},
        onBlogClick = {},
        onLocationsClick = {},
        onContactUsClick = {},
        onSignOutClick = {},
        onAdminPanelClick = {}
    )
}