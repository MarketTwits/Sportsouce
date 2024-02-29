package com.markettwits.members.member_edit.presentation.components.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.base_extensions.date.mapToString
import com.markettwits.core_ui.components.OnBackgroundCard
import com.markettwits.core_ui.components.checkbox.CheckBoxTertiaryBase
import com.markettwits.core_ui.components.textField.ItemsTextFiledDialog
import com.markettwits.core_ui.components.textField.OutlinePhoneTextFiled
import com.markettwits.core_ui.components.textField.OutlinedTextFieldBase
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.ui_style.CalendarTextFiled
import com.markettwits.core_ui.ui_style.DropDownSpinner
import com.markettwits.members.member_common.domain.ProfileMember
import com.markettwits.teams_city.domain.Team
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMemberTextFieldsContent(
    modifier: Modifier = Modifier,
    onMemberChange: (ProfileMember) -> Unit,
    member: ProfileMember,
    teams: List<Team>,
) {

    OnBackgroundCard(modifier = modifier) {
        val modifierInner = Modifier.padding(5.dp)
        Column(modifierInner) {
            OutlinedTextFieldBase(
                modifier = modifierInner,
                value = member.name,
                onValueChange = { newValue -> onMemberChange(member.copy(name = newValue)) },
                label = "Имя"
            )
            OutlinedTextFieldBase(
                modifier = modifierInner,
                value = member.surname,
                onValueChange = { newValue -> onMemberChange(member.copy(surname = newValue)) },
                label = "Фамилия"
            )
            OutlinePhoneTextFiled(
                modifier = modifierInner,
                value = member.phone,
                onValueChange = { newValue -> onMemberChange(member.copy(phone = newValue)) },
                label = "Номер телефона"
            )
            OutlinedTextFieldBase(
                modifier = modifierInner,
                value = member.email,
                onValueChange = { newValue -> onMemberChange(member.copy(email = newValue)) },
                label = "Почта"
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
                    onMemberChange(member.copy(birthday = newDate.mapToString()))
                },
            )
            CalendarTextFiled(
                modifier = modifierInner,
                textFiled = {
                    OutlinedTextFieldBase(
                        modifier = it,
                        label = "День рождения",
                        value = member.birthday,
                        isEnabled = false
                    ) {}
                },
                onValueChanged = {
                    onMemberChange(member.copy(birthday = it))
                }
            )
            val sexList = listOf("Мужской", "Женский")
            DropDownSpinner(
                modifier = modifierInner,
                itemList = sexList,
                selectedItem = member.gender,
                onItemSelected = { id, item ->
                    onMemberChange(member.copy(gender = item))
                },
                textFiled = {
                    OutlinedTextFieldBase(
                        label = "Пол",
                        value = member.gender,
                        isEnabled = false
                    ) {}
                }
            )
            ItemsTextFiledDialog(
                modifier = modifierInner,
                label = "Команда",
                value = member.team,
                items = teams
                    .sortedBy { it.name }
                    .map { it.name },
            ) { newValue -> onMemberChange(member.copy(team = newValue)) }

            val typeList = remember {
                listOf("Родственник", "Друг", "Партнер по команде")
            }
            DropDownSpinner(
                modifier = modifierInner,
                itemList = typeList,
                selectedItem = member.type,
                onItemSelected = { id, item ->
                    onMemberChange(member.copy(type = item))
                },
                textFiled = {
                    OutlinedTextFieldBase(
                        label = "Кем вам приходится участник ?",
                        value = member.type,
                        isEnabled = false
                    ) {}
                }
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifierInner
            ) {
                CheckBoxTertiaryBase(checked = !member.child, onValueChanged = {
                    onMemberChange(member.copy(child = !it))
                })
                Text(
                    text = "Взрослный",
                    fontFamily = FontNunito.bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifierInner
            ) {
                CheckBoxTertiaryBase(checked = member.child, onValueChanged = {
                    onMemberChange(member.copy(child = it))
                })
                Text(
                    text = "Ребенок",
                    fontFamily = FontNunito.bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}