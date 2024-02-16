package com.markettwits.edit_profile.edit_profile.presentation.new_components.social_network

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.components.TopBarWithClip
import com.markettwits.core_ui.theme.SportSouceTheme
import com.markettwits.profile.presentation.component.edit_profile.presentation.EditProfileUiPage

@Composable
fun ProfileSocialNetworkScreen() {
    SportSouceTheme {
        Scaffold(topBar = {
            TopBarWithClip(title = "Редактировать профиль", goBack = {

            })
        }) {
            ProfileSocialNetworkContent(
                modifier = Modifier
                    .padding(10.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(top = it.calculateTopPadding()),
                user = fake(),
                onUserChange = {}
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ProfileSocialNetworkScreenPreview() {
    ProfileSocialNetworkScreen()
}

private fun fake() = EditProfileUiPage.MySocialNetwork("", "", "", "")