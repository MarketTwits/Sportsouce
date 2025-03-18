package com.markettwits.edit_profile.edit_profile_info.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.textField.CalendarTextFiled
import com.markettwits.core_ui.items.components.textField.DropDownSpinner
import com.markettwits.core_ui.items.components.textField.ItemsTextFiledDialog
import com.markettwits.core_ui.items.components.textField.OutlinePhoneTextFiled
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.edit_profile.edit_profile_info.domain.models.UserData
import com.markettwits.teams_city.domain.City
import com.markettwits.teams_city.domain.Team


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
            OutlinePhoneTextFiled(
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
