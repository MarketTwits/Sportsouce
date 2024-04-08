package com.markettwits.profile.presentation.component.edit_profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.edit_profile.edit_profile.presentation.EditProfileUiPage
import com.markettwits.edit_profile.edit_profile.presentation.components.MyProfileTextField


@Composable
fun MyInfoPage(page: EditProfileUiPage.MyInfo, onUserChange: (EditProfileUiPage.MyInfo) -> Unit) {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .background(Color.White)
    ) {
        val modifier = Modifier.padding(5.dp)
        MyProfileTextField(
            modifier = modifier,
            value = page.description,
            onValueChange = { newValue -> onUserChange(page.copy(description = newValue)) },
            label = "Обо мне"
        )
    }
}

