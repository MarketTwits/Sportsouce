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
fun MySocialNetworkPage(
    user: EditProfileUiPage.MySocialNetwork,
    onUserChange: (EditProfileUiPage.MySocialNetwork) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .background(Color.White)
    ) {
        val modifier = Modifier.padding(5.dp)
        MyProfileTextField(
            modifier = modifier,
            value = user.telegram ?: "",
            onValueChange = { newValue -> onUserChange(user.copy(telegram = newValue)) },
            label = "Telegram"
        )
        MyProfileTextField(
            modifier = modifier,
            value = user.whatsApp ?: "",
            onValueChange = { newValue -> onUserChange(user.copy(whatsApp = newValue)) },
            label = "WhatsApp"
        )
        MyProfileTextField(
            modifier = modifier,
            value = user.vk ?: "",
            onValueChange = { newValue -> onUserChange(user.copy(vk = newValue)) },
            label = "ВКонтакте"
        )
        MyProfileTextField(
            modifier = modifier,
            value = user.instagram ?: "",
            onValueChange = { newValue -> onUserChange(user.copy(instagram = newValue)) },
            label = "Instagram"
        )
    }
}

