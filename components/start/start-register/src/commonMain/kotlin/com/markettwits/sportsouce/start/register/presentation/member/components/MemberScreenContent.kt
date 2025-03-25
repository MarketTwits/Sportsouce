package com.markettwits.sportsouce.start.register.presentation.member.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.textField.CalendarTextFiled
import com.markettwits.core_ui.items.components.textField.DropDownSpinner
import com.markettwits.core_ui.items.components.textField.ItemsTextFiledDialog
import com.markettwits.core_ui.items.components.textField.OutlinePhoneTextFiled
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import com.markettwits.sportsouce.start.register.domain.StartStatement


@Composable
fun MemberScreenContent(
    modifier: Modifier = Modifier,
    userNumber: Int,
    statement: StartStatement,
    members: List<ProfileMember>,
    onValueChanged: (StartStatement) -> Unit,
    onClickContinue: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(15.dp)
            .verticalScroll(rememberScrollState())
    ) {
        MemberContactFace(checked = statement.contactPerson, onValueChanged = {
            onValueChanged(statement.copy(contactPerson = it))
        })
        MemberSelectMember(
            modifier = Modifier.padding(vertical = 5.dp),
            members = members
        ) {
            onValueChanged(memberSelectApply(it, statement))
        }
        Text(
            modifier = Modifier.padding(vertical = 5.dp),
            text = "Участник ${userNumber + 1}",
            fontSize = 16.sp,
            fontFamily = FontNunito.bold(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.tertiary
        )
        OutlinedTextFieldBase(
            modifier = Modifier.padding(vertical = 5.dp),
            label = "Имя",
            value = statement.name,
            onValueChange = {
                onValueChanged(statement.copy(name = it))
            }
        )
        OutlinedTextFieldBase(
            modifier = Modifier.padding(vertical = 5.dp),
            label = "Фамилия",
            value = statement.surname,
            onValueChange = {
                onValueChanged(statement.copy(surname = it))
            }
        )
        CalendarTextFiled(
            textFiled = { calendarModifier ->
                OutlinedTextFieldBase(
                    modifier = calendarModifier
                        .padding(vertical = 5.dp),
                    isEnabled = false,
                    label = "День рождения",
                    value = statement.birthday,
                    onValueChange = {
                        onValueChanged(statement.copy(birthday = it))
                    }
                )
            }, onValueChanged = {
                onValueChanged(statement.copy(birthday = it))
            })
        OutlinedTextFieldBase(
            modifier = Modifier.padding(vertical = 5.dp),
            label = "Возраст",
            isEnabled = false,
            value = statement.age,
            onValueChange = {}
        )

        if (statement.contactPerson) {
            OutlinedTextFieldBase(
                modifier = Modifier.padding(vertical = 5.dp),
                label = "Почта",
                value = statement.email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                onValueChange = {
                    onValueChanged(statement.copy(email = it))
                }
            )
            OutlinePhoneTextFiled(
                modifier = Modifier.padding(vertical = 5.dp),
                label = "Номер телефона",
                value = statement.phone,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                onValueChange = {
                    onValueChanged(statement.copy(phone = it))
                }
            )
        }
        DropDownSpinner(
            modifier = Modifier.padding(vertical = 5.dp),
            selectedItem = statement.sex, onItemSelected = { id, value ->
                onValueChanged(statement.copy(sex = value))
            }, itemList = statement.sexList.map { it.name }
        ) {
            OutlinedTextFieldBase(
                label = "Пол",
                value = statement.sex,
                isEnabled = false
            ) {}
        }
        CityFiled(
            modifier = Modifier.padding(vertical = 5.dp),
            statement = statement,
            onValueChanged = onValueChanged::invoke
        )
        TeamFiled(
            modifier = Modifier.padding(vertical = 5.dp),
            statement = statement,
            onValueChanged = onValueChanged::invoke
        )
        MemberContinueButton(onClickContinue = {
            onClickContinue()
        })
    }
}

@Composable
fun CityFiled(
    modifier: Modifier = Modifier,
    statement: StartStatement,
    onValueChanged: (StartStatement) -> Unit
) {
    var cityChecked by rememberSaveable {
        mutableStateOf(false)
    }
    if (cityChecked) {
        OutlinedTextFieldBase(
            label = "Город",
            value = statement.city,
            onValueChange = {
                onValueChanged(statement.copy(city = it))
            }
        )
    } else {
        ItemsTextFiledDialog(
            label = "Город",
            value = statement.city,
            items = statement.cities.map { it.name },
            onValueChanged = {
                onValueChanged(statement.copy(city = it))
            })
    }
    FilterPosition(
        item = "В списке нет моего города",
        checked = cityChecked,
        onClick = { cityChecked = !cityChecked }
    )
}

@Composable
fun TeamFiled(
    modifier: Modifier = Modifier,
    statement: StartStatement,
    onValueChanged: (StartStatement) -> Unit
) {
    Column(modifier = modifier) {
        var teamChecked by rememberSaveable {
            mutableStateOf(false)
        }
        if (teamChecked) {
            OutlinedTextFieldBase(
                label = "Команда",
                value = "Лично",
                isEnabled = false,
                onValueChange = {
                    onValueChanged(statement.copy(team = it))
                }
            )
        } else {
            ItemsTextFiledDialog(
                label = "Команда",
                value = statement.team,
                items = statement.teams.map { it.name },
                onValueChanged = {
                    onValueChanged(statement.copy(team = it))
                })
        }
        FilterPosition(
            item = "Я участвую лично, нет команды",
            checked = teamChecked,
            onClick = {
                onValueChanged(statement.copy(team = "Лично"))
                teamChecked = !teamChecked
            }
        )
    }

}

@Composable
fun FilterPosition(
    item: String,
    checked: Boolean,
    onClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(item)
            },
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Checkbox(
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.tertiary,
                checkmarkColor = MaterialTheme.colorScheme.onTertiary
            ),
            checked = checked,
            onCheckedChange = { onClick(item) })
        Text(
            text = item,
            color = MaterialTheme.colorScheme.tertiary,
            fontFamily = FontNunito.medium(),
            fontSize = 14.sp
        )
    }
}