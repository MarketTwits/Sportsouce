package com.markettwits.start.presentation.registration.components.content

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.markettwits.start.domain.StartStatement
import com.markettwits.start.presentation.registration.components.RegistrationTextField

@Composable
fun StartRegistrationSuccessContent(statement : StartStatement,onValueChanged : (StartStatement) -> Unit) {
    Column {
        RegistrationTextField(
            label = "Имя",
            value = statement.name,
            onValueChange = {
                onValueChanged(statement.copy(name = it))
            }
        )
        RegistrationTextField(
            label = "Фамилия",
            value = statement.surname,
            onValueChange = {
                onValueChanged(statement.copy(surname = it))
            }
        )
        RegistrationTextField(
            label = "День рождения",
            value = statement.birthday,
            onValueChange = {
                onValueChanged(statement.copy(birthday = it))
            }
        )
        RegistrationTextField(
            label = "Почта",
            value = statement.email,
            onValueChange = {
                onValueChanged(statement.copy(email = it))
            }
        )
        RegistrationTextField(
            label = "Номер телефона",
            value = statement.phone,
            onValueChange = {
                onValueChanged(statement.copy(phone = it))
            }
        )
        RegistrationTextField(
            label = "Пол",
            value = statement.sex,
            onValueChange = {
                onValueChanged(statement.copy(sex = it))
            }
        )
        RegistrationTextField(
            label = "Город",
            value = statement.city,
            onValueChange = {
                onValueChanged(statement.copy(city = it))
            }
        )
        RegistrationTextField(
            label = "Команда",
            value = statement.team,
            onValueChange = {
                onValueChanged(statement.copy(team = it))
            }
        )
        RegistrationTextField(
            label = "Промокод",
            value = statement.promocode,
            onValueChange = {
                onValueChanged(statement.copy(promocode = it))
            }
        )
    }
}