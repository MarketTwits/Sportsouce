package com.markettwits.sportsouce.profile.members.member_add_edit.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.selection.SauceRadioRow
import com.markettwits.core_ui.items.components.textField.*
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.profile.members.member_common.domain.MemberRelationship
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import com.markettwits.sportsouce.teams_city.domain.Team

@Composable
fun ImprovedEditMemberContent(
    modifier: Modifier = Modifier,
    onMemberChange: (ProfileMember) -> Unit,
    member: ProfileMember,
    teams: List<Team>,
    enabled: Boolean = true,
    teamsLoading: Boolean = false,
    teamsError: Boolean = false,
    onRetryTeams: () -> Unit = {},
) {
    val focus = LocalFocusManager.current
    val next = KeyboardActions(onNext = { focus.moveFocus(FocusDirection.Down) })
    val keyboard = KeyboardOptions(imeAction = ImeAction.Next)
    Column(modifier.padding(SauceSpacing.large), verticalArrangement = Arrangement.spacedBy(SauceSpacing.large)) {
        MemberFormSection("Личные данные") {
            OutlinedTextFieldBase(label = "Имя", value = member.name, isEnabled = enabled,
                keyboardOptions = keyboard, keyboardActions = next, onValueChange = { onMemberChange(member.copy(name = it)) })
            OutlinedTextFieldBase(label = "Фамилия", value = member.surname, isEnabled = enabled,
                keyboardOptions = keyboard, keyboardActions = next, onValueChange = { onMemberChange(member.copy(surname = it)) })
            CalendarTextFiled(enabled = enabled, value = member.birthday, textFiled = {
                OutlinedTextFieldBase(modifier = it, label = "Дата рождения", value = member.birthday,
                    readOnly = true, isEnabled = enabled, onValueChange = {})
            }, onValueChanged = { onMemberChange(member.copy(birthday = it)) })
            DropDownSpinner(selectedItem = member.gender, itemList = listOf("Мужской", "Женский"), enabled = enabled,
                onItemSelected = { _, value -> onMemberChange(member.copy(gender = value)) }, textFiled = {
                    OutlinedTextFieldBase(label = "Пол", value = member.gender, readOnly = true, onValueChange = {})
                })
        }
        MemberFormSection("Регистрация") {
            DropDownSpinner(selectedItem = member.type, itemList = MemberRelationship.optionsFor(member.type), enabled = enabled,
                onItemSelected = { _, value -> onMemberChange(member.copy(type = value)) }, textFiled = {
                    OutlinedTextFieldBase(label = "Кем вам приходится участник?", value = member.type, readOnly = true, onValueChange = {})
                })
            TeamField(value = member.team, teams = teams, enabled = enabled,
                onValueChanged = { onMemberChange(member.copy(team = it)) })
            if (teamsLoading) Text("Загружаем список команд…", style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted)
            if (teamsError) {
                Text("Список команд не загрузился. Можно выбрать участие лично или повторить загрузку.",
                    style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted)
                com.markettwits.core_ui.items.components.buttons.SauceButton("Загрузить команды", onRetryTeams,
                    variant = com.markettwits.core_ui.items.components.buttons.SauceButtonVariant.Text, enabled = enabled)
            }
            Column(Modifier.fillMaxWidth().selectableGroup()) {
                SauceRadioRow("Взрослый", selected = !member.child, enabled = enabled,
                    onClick = { onMemberChange(member.copy(child = false)) })
                SauceRadioRow("Ребёнок — без телефона и почты", selected = member.child, enabled = enabled,
                    onClick = { onMemberChange(member.copy(child = true)) })
            }
        }
        if (!member.child) MemberFormSection("Контакты") {
            OutlinePhoneTextFiled(label = "Телефон (необязательно)", value = member.phone, isEnabled = enabled,
                keyboardOptions = keyboard.copy(keyboardType = KeyboardType.Phone), keyboardActions = next,
                onValueChange = { onMemberChange(member.copy(phone = it)) })
            OutlinedTextFieldBase(label = "Email (необязательно)", value = member.email, isEnabled = enabled,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
                onValueChange = { onMemberChange(member.copy(email = it)) })
        }
    }
}

@Composable
private fun MemberFormSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    OnBackgroundCard {
        Column(Modifier.fillMaxWidth().padding(SauceSpacing.large), verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = SauceTheme.colors.textPrimary)
            content()
        }
    }
}

@Composable
private fun TeamField(value: String, teams: List<Team>, enabled: Boolean, onValueChanged: (String) -> Unit) {
    val personal = "Я участвую лично"
    val options = listOf(personal) + teams.map { it.name }.filter { it != "Лично" }.distinct().sorted() +
        listOf(value).filter { it.isNotBlank() && it != "Лично" && teams.none { team -> team.name == it } }
    ItemsTextFiledDialog(label = "Команда", value = if (value == "Лично") personal else value,
        items = options.distinct(), enabled = enabled,
        onValueChanged = { onValueChanged(if (it == personal) "Лично" else it) })
}
