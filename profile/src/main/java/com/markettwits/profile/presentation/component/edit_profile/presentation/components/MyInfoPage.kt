package com.markettwits.profile.presentation.component.edit_profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.profile.presentation.common.text_filed.MyProfileTextField
import com.markettwits.profile.presentation.component.edit_profile.presentation.EditProfileUiPage


@Composable
fun MyInfoPage(page: EditProfileUiPage.MyInfo, onUserChange: (EditProfileUiPage.MyInfo) -> Unit) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .background(Color.White)
    ) {
        MyProfileTextField(
            value = page.description,
            onValueChange = { newValue -> onUserChange(page.copy(description = newValue)) },
            label = "Обо мне"
        )
    }
}

