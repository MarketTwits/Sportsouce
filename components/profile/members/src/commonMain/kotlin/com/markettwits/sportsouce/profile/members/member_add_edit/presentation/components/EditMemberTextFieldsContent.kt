package com.markettwits.sportsouce.profile.members.member_add_edit.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.checkbox.CheckBoxBase
import com.markettwits.core_ui.items.components.textField.*
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import com.markettwits.sportsouce.teams_city.domain.Team

@Suppress("NonSkippableComposable")
@Composable
fun EditMemberTextFieldsContent(
    modifier: Modifier = Modifier,
    onMemberChange: (ProfileMember) -> Unit,
    member: ProfileMember,
    teams: List<Team>,
) {
    OnBackgroundCard(modifier = modifier) {
        val modifierInner = Modifier.padding(8.dp)
        val focusManager = LocalFocusManager.current
        
        Column(modifierInner) {
            OutlinedTextFieldBase(
                modifier = modifierInner,
                value = member.name,
                onValueChange = { newValue -> onMemberChange(member.copy(name = newValue)) },
                label = "Имя",
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            OutlinedTextFieldBase(
                modifier = modifierInner,
                value = member.surname,
                onValueChange = { newValue -> onMemberChange(member.copy(surname = newValue)) },
                label = "Фамилия",
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            OutlinePhoneTextFiled(
                modifier = modifierInner,
                value = member.phone,
                onValueChange = { newValue -> onMemberChange(member.copy(phone = newValue)) },
                label = "Номер телефона",
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Phone
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            OutlinedTextFieldBase(
                modifier = modifierInner,
                value = member.email,
                onValueChange = { newValue -> onMemberChange(member.copy(email = newValue)) },
                label = "Почта",
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            CalendarTextFiled(
                modifier = modifierInner,
                textFiled = {
                    OutlinedTextFieldBase(
                        modifier = it,
                        label = "День рождения",
                        value = member.birthday,
                        isEnabled = false,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = {
                        onMemberChange(member.copy(child = false))
                    })
            ) {
                CheckBoxBase(checked = !member.child, onValueChanged = {
                    onMemberChange(member.copy(child = false))
                })
                Text(
                    text = "Взрослный",
                    fontFamily = FontNunito.medium(),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = {
                        onMemberChange(member.copy(child = true))
                    })
            ) {
                CheckBoxBase(checked = member.child, onValueChanged = {
                    onMemberChange(member.copy(child = true))
                })
                Text(
                    text = "Ребенок",
                    fontFamily = FontNunito.medium(),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}