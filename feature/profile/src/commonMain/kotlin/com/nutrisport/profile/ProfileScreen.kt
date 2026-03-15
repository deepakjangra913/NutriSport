package com.nutrisport.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nutrisport.shared.BebasNeueFont
import com.nutrisport.shared.FontSize
import com.nutrisport.shared.IconPrimary
import com.nutrisport.shared.Resources
import com.nutrisport.shared.Surface
import com.nutrisport.shared.TextPrimary
import com.nutrisport.shared.component.PrimaryButton
import com.nutrisport.shared.component.ProfileForm
import com.nutrisport.shared.domain.Country
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBackPress: () -> Unit
) {
    val viewModel = koinViewModel<ProfileViewModel>()
    var country by remember { mutableStateOf(Country.India) }

    Scaffold(
        contentColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Profile",
                        fontSize = FontSize.LARGE,
                        fontFamily = BebasNeueFont(),
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackPress()
                    }) {
                        Icon(
                            painter = painterResource(Resources.Icon.BackArrow),
                            contentDescription = "Close Icon",
                            tint = IconPrimary
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                )
                .padding(horizontal = 24.dp)
                .padding(top = 12.dp, bottom = 24.dp)
        ) {
            ProfileForm(
                modifier = Modifier.weight(1f),
                firstName = "Deepak",
                country = country,
                onCountrySelect = { selectedCountry ->
                    country = selectedCountry
                },
                onFirstNameChange = {},
                lastName = "",
                onLastNameChange = {},
                email = "",
                city = "",
                onCityChange = {},
                postalCode = null,
                onPostalCodeChange = {},
                address = "",
                onAddressChange = {},
                phoneNumber = "",
                onPhoneNumberChange = {}
            )

            Spacer(modifier = Modifier.height(12.dp))

            PrimaryButton(
                text = "Update",
                icon = Resources.Icon.Checkmark,
                onClick = {

                }
            )
        }
    }
}
