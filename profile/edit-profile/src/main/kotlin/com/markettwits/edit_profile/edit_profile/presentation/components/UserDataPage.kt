package com.markettwits.edit_profile.edit_profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.edit_profile.edit_profile.presentation.EditProfileUiPage
import com.markettwits.profile.presentation.component.edit_profile.presentation.components.AutoComplete
import com.markettwits.profile.presentation.component.edit_profile.presentation.components.mapToString
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.util.Locale.getDefault


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDataPage(
    page: EditProfileUiPage.UserData,
    onUserChange: (EditProfileUiPage.UserData) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .background(Color.White)
    ) {
        val modifier = Modifier.padding(5.dp)
        MyProfileTextField(
            modifier = modifier,
            value = page.name,
            onValueChange = { newValue -> onUserChange(page.copy(name = newValue)) },
            label = "Имя"
        )
        MyProfileTextField(
            modifier = modifier,
            value = page.surname,
            onValueChange = { newValue -> onUserChange(page.copy(surname = newValue)) },
            label = "Фамилия"
        )
        MyProfileTextField(
            modifier = modifier,
            value = page.sex,
            onValueChange = { newValue -> onUserChange(page.copy(sex = newValue)) },
            label = "Пол"
        )
        val calendarState = rememberUseCaseState()
        CalendarDialog(
            state = calendarState,
            config = CalendarConfig(
                locale = getDefault(),
                yearSelection = true,
                monthSelection = true,
                style = CalendarStyle.MONTH,
            ),
            selection = CalendarSelection.Date { newDate ->
                onUserChange(page.copy(birthday = newDate.mapToString()))
            },
        )
        DateProfileTextField(
            modifier = modifier,
            value = page.birthday,
            onValueChange = { newValue -> onUserChange(page.copy(birthday = newValue)) },
            label = "День рождения"
        ) {
            calendarState.show()
        }

        MyProfileTextField(
            modifier = modifier,
            value = page.phoneNumber,
            onValueChange = { newValue -> onUserChange(page.copy(phoneNumber = newValue)) },
            label = "Номер телефона"
        )
        AutoComplete(
            categories = page._cities.map { it.name },
            value = page.city, label = "Город",
            onValueChange = { newValue -> onUserChange(page.copy(city = newValue)) })

        AutoComplete(
            categories = page._teams.map { it.name },
            value = page.team, label = "Команда",
            onValueChange = { newValue -> onUserChange(page.copy(team = newValue)) })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileDatePicker() {
    val calendarState = rememberUseCaseState()
    CalendarDialog(state = calendarState, selection = CalendarSelection.Date {

    })
}
