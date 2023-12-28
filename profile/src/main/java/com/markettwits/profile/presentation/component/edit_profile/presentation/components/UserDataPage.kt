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
fun UserDataPage(
    page: EditProfileUiPage.UserData,
    onUserChange: (EditProfileUiPage.UserData) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .background(Color.White)
    ) {
        MyProfileTextField(
            value = page.name,
            onValueChange = { newValue -> onUserChange(page.copy(name = newValue)) },
            label = "Имя"
        )
        MyProfileTextField(
            value = page.surname,
            onValueChange = { newValue -> onUserChange(page.copy(surname = newValue)) },
            label = "Фамилия"
        )
        MyProfileTextField(
            value = page.sex,
            onValueChange = { newValue -> onUserChange(page.copy(sex = newValue)) },
            label = "Пол"
        )
        MyProfileTextField(
            value = page.birthday,
            onValueChange = { newValue -> onUserChange(page.copy(birthday = newValue)) },
            label = "День рождения"
        )
        MyProfileTextField(
            value = page.phoneNumber,
            onValueChange = { newValue -> onUserChange(page.copy(phoneNumber = newValue)) },
            label = "Номер телефона"
        )
        MyProfileTextField(
            value = page.city,
            onValueChange = { newValue -> onUserChange(page.copy(city = newValue)) },
            label = "Город"
        )
        MyProfileTextField(
            value = page.team,
            onValueChange = { newValue -> onUserChange(page.copy(team = newValue)) },
            label = "Команда"
        )

    }
}

