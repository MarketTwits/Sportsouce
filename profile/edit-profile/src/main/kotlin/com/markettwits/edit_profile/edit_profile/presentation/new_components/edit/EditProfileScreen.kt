package com.markettwits.edit_profile.edit_profile.presentation.new_components.edit

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
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
import com.markettwits.edit_profile.edit_profile.presentation.new_components.edit.components.EditProfileMenu

@Composable
fun EditProfileScreen() {
    SportSouceTheme {
        Scaffold(topBar = {
            TopBarWithClip(title = "Редактировать профиль", goBack = {

            })
        }) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(top = it.calculateTopPadding())
                    .padding(10.dp)
            ) {
                EditProfileMenu(menu = profileMenu(), onClickItem = {

                })
                EditProfileMenu(
                    modifier = Modifier.padding(vertical = 10.dp),
                    menu = securityMenu(),
                    onClickItem = {
                    })
            }
        }
    }
}


@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun EditProfileScreenPreview() {
    EditProfileScreen()
}