package com.markettwits.members.member_edit.presentation.components.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.base_extensions.date.mapToString
import com.markettwits.core_ui.components.OnBackgroundCard
import com.markettwits.core_ui.components.textField.ItemsTextFiledDialog
import com.markettwits.core_ui.components.textField.OutlinedTextFieldBase
import com.markettwits.core_ui.ui_style.CalendarTextFiled
import com.markettwits.core_ui.ui_style.DropDownSpinner
import com.markettwits.members.common.domain.ProfileMember
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
            OutlinedTextFieldBase(
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
            var isChild by remember {
                mutableStateOf<Boolean>(member.child)
            }
            Row {
                Checkbox(checked = isChild, onCheckedChange = {
                    isChild = !isChild
                })
                Text(text = "Взрослый")
            }
            Row {
                Checkbox(checked = !isChild, onCheckedChange = {
                    isChild = !isChild
                })
                Text(text = "Ребенок")
            }
        }
    }
}