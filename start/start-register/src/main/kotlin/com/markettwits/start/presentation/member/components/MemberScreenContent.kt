package com.markettwits.start.presentation.member.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.textField.CalendarTextFiled
import com.markettwits.core_ui.components.textField.DropDownSpinner
import com.markettwits.core_ui.components.textField.OutlinePhoneTextFiled
import com.markettwits.core_ui.components.textField.OutlinedTextFieldBase
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.members.member_common.domain.ProfileMember
import com.markettwits.start.domain.StartStatement
import com.markettwits.start.presentation.order.presentation.components.extra.fileds.CityFiled
import com.markettwits.start.presentation.order.presentation.components.extra.fileds.TeamFiled
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalMaterial3Api::class)
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
            fontFamily = FontNunito.bold,
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
            }, itemList = statement.sexList.map { it.name }.toImmutableList()
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