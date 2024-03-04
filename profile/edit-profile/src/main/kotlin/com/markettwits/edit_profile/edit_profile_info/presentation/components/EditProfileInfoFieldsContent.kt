package com.markettwits.edit_profile.edit_profile_info.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.components.OnBackgroundCard
import com.markettwits.core_ui.components.textField.ItemsTextFiledDialog
import com.markettwits.core_ui.components.textField.OutlinedTextFieldBase
import com.markettwits.core_ui.ui_style.CalendarTextFiled
import com.markettwits.core_ui.ui_style.DropDownSpinner
import com.markettwits.edit_profile.edit_profile_info.domain.models.UserData
import com.markettwits.profile.presentation.component.edit_profile.presentation.components.mapToString
import com.markettwits.teams_city.domain.City
import com.markettwits.teams_city.domain.Team
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
    OnBackgroundCard(modifier = modifier) {
        val modifierInner = Modifier.padding(5.dp)
        Column(modifierInner) {
            OutlinedTextFieldBase(
                modifier = modifierInner,
                value = user.name,
                onValueChange = { newValue -> onUserChange(user.copy(name = newValue)) },
                label = "Имя"
            )
            OutlinedTextFieldBase(
                modifier = modifierInner,
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
                modifier = modifierInner,
                textFiled = {
                    OutlinedTextFieldBase(
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
            OutlinedTextFieldBase(
                modifier = modifierInner,
                value = user.phoneNumber,
                onValueChange = { newValue -> onUserChange(user.copy(phoneNumber = newValue)) },
                label = "Номер телефона"
            )
            val sexList = listOf("Мужской", "Женский")
            DropDownSpinner(
                modifier = modifierInner,
                itemList = sexList,
                selectedItem = user.sex,
                onItemSelected = { id, item ->
                    onUserChange(user.copy(sex = item))
                },
                textFiled = {
                    OutlinedTextFieldBase(
                        label = "Пол",
                        value = user.sex,
                        isEnabled = false
                    ) {}
                }
            )
            ItemsTextFiledDialog(
                modifier = modifierInner,
                label = "Город",
                value = user.city,
                items = cities
                    .sortedBy { it.name }
                    .map { it.name },
            ) { newValue -> onUserChange(user.copy(city = newValue)) }
            ItemsTextFiledDialog(
                modifier = modifierInner,
                label = "Команда",
                value = user.team,
                items = teams
                    .sortedBy { it.name }
                    .map { it.name },
            ) { newValue -> onUserChange(user.copy(team = newValue)) }
        }
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