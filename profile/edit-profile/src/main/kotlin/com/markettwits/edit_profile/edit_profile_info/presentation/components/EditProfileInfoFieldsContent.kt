package com.markettwits.edit_profile.edit_profile_info.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.components.textField.ItemsTextFiledDialog
import com.markettwits.core_ui.components.textField.TextFieldBase
import com.markettwits.core_ui.ui_style.CalendarTextFiled
import com.markettwits.core_ui.ui_style.DropDownSpinner
import com.markettwits.edit_profile.edit_profile_info.domain.models.City
import com.markettwits.edit_profile.edit_profile_info.domain.models.Team
import com.markettwits.edit_profile.edit_profile_info.domain.models.UserData
import com.markettwits.profile.presentation.component.edit_profile.presentation.components.mapToString
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileInfoFieldsContent(
    modifier: Modifier = Modifier,
    user: UserData,
    teams: List<Team>,
    cities: List<City>,
    onUserChange: (UserData) -> Unit
) {
    Column(
        modifier = modifier
            .clip(Shapes.medium)
            .background(MaterialTheme.colorScheme.secondary)
            .padding(10.dp)
    ) {
        val modifier = Modifier.padding(5.dp)
        TextFieldBase(
            modifier = modifier,
            value = user.name,
            onValueChange = { newValue -> onUserChange(user.copy(name = newValue)) },
            label = "Имя"
        )
        TextFieldBase(
            modifier = modifier,
            value = user.surname,
            onValueChange = { newValue -> onUserChange(user.copy(surname = newValue)) },
            label = "Фамилия"
        )
        val calendarState = rememberUseCaseState()
        CalendarDialog(
            state = calendarState,
            config = CalendarConfig(
                locale = Locale.getDefault(),
                yearSelection = true,
                monthSelection = true,
                style = CalendarStyle.MONTH,
            ),
            selection = CalendarSelection.Date { newDate ->
                onUserChange(user.copy(birthday = newDate.mapToString()))
            },
        )
        CalendarTextFiled(
            modifier = modifier,
            textFiled = {
                TextFieldBase(
                    modifier = it,
                    label = "День рождения",
                    value = user.birthday,
                    isEnabled = false
                ) {}
            },
            onValueChanged = {
                onUserChange(user.copy(birthday = it))
            }
        )
        TextFieldBase(
            modifier = modifier,
            value = user.phoneNumber,
            onValueChange = { newValue -> onUserChange(user.copy(phoneNumber = newValue)) },
            label = "Номер телефона"
        )
        val sexList = listOf("Мужской", "Женский")
        DropDownSpinner(
            modifier = modifier,
            itemList = sexList,
            selectedItem = user.sex,
            onItemSelected = { id, item ->
                onUserChange(user.copy(sex = item))
            },
            textFiled = {
                TextFieldBase(
                    label = "Пол",
                    value = user.sex,
                    isEnabled = false
                ) {}
            }
        )
        ItemsTextFiledDialog(
            modifier = modifier,
            label = "Город",
            value = user.city,
            items = cities.map { it.name },
        ) { newValue -> onUserChange(user.copy(city = newValue)) }
        ItemsTextFiledDialog(
            modifier = modifier,
            label = "Команда",
            value = user.team,
            items = teams.map { it.name },
        ) { newValue -> onUserChange(user.copy(team = newValue)) }
    }

}

@Preview
@Composable
private fun EditProfileInfoFieldsContentPreview() {
    EditProfileInfoFieldsContent(
        teams = emptyList(),
        cities = emptyList(),
        user = UserData(0, "d", "f", "s", "b", "", "fw", "fw", "")
    ) {}
}