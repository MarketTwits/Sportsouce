package com.markettwits.sportsouce.profile.members.member_add_edit.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
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
import com.markettwits.core_ui.items.components.checkbox.CheckBoxBase
import com.markettwits.core_ui.items.components.textField.*
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import com.markettwits.sportsouce.teams_city.domain.Team

@Composable
fun ImprovedEditMemberContent(
    modifier: Modifier = Modifier,
    onMemberChange: (ProfileMember) -> Unit,
    member: ProfileMember,
    teams: List<Team>,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        PersonalInfoSection(
            modifier = Modifier.padding(horizontal = 16.dp),
            member = member,
            onMemberChange = onMemberChange
        )

        ContactInfoSection(
            modifier = Modifier.padding(horizontal = 16.dp),
            member = member,
            onMemberChange = onMemberChange
        )

        AdditionalInfoSection(
            modifier = Modifier.padding(horizontal = 16.dp),
            member = member,
            teams = teams,
            onMemberChange = onMemberChange
        )

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun PersonalInfoSection(
    modifier: Modifier = Modifier,
    member: ProfileMember,
    onMemberChange: (ProfileMember) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Личная информация",
                    fontFamily = FontNunito.bold(),
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            OutlinedTextFieldBase(
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

            CalendarTextFiled(
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
        }
    }
}

@Composable
private fun ContactInfoSection(
    modifier: Modifier = Modifier,
    member: ProfileMember,
    onMemberChange: (ProfileMember) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Контактная информация",
                    fontFamily = FontNunito.bold(),
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            OutlinePhoneTextFiled(
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
        }
    }
}

@Composable
private fun AdditionalInfoSection(
    modifier: Modifier = Modifier,
    member: ProfileMember,
    teams: List<Team>,
    onMemberChange: (ProfileMember) -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Дополнительная информация",
                    fontFamily = FontNunito.bold(),
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            ItemsTextFiledDialog(
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
                itemList = typeList,
                selectedItem = member.type,
                onItemSelected = { id, item ->
                    onMemberChange(member.copy(type = item))
                },
                textFiled = {
                    OutlinedTextFieldBase(
                        label = "Кем вам приходится участник?",
                        value = member.type,
                        isEnabled = false
                    ) {}
                }
            )

            Text(
                text = "Категория участника",
                fontFamily = FontNunito.semiBoldBold(),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 4.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                AgeOptionCard(
                    modifier = Modifier.weight(1f),
                    label = "Взрослый",
                    isSelected = !member.child,
                    onClick = { onMemberChange(member.copy(child = false)) },
                    isChild = false
                )

                AgeOptionCard(
                    modifier = Modifier.weight(1f),
                    label = "Ребенок",
                    isSelected = member.child,
                    onClick = { onMemberChange(member.copy(child = true)) },
                    isChild = true
                )
            }
        }
    }
}

@Composable
private fun AgeOptionCard(
    modifier: Modifier = Modifier,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    isChild: Boolean,
) {
    val selectedColor = if (isChild)
        SportSouceColor.SportSouceRegistryOpenGreen
    else
        MaterialTheme.colorScheme.secondary

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .height(56.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected)
            MaterialTheme.colorScheme.tertiaryContainer
        else
            MaterialTheme.colorScheme.primaryContainer,
        border = if (isSelected)
            BorderStroke(2.dp, selectedColor)
        else
            BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CheckBoxBase(
                checked = isSelected,
                onValueChanged = { onClick() },
                colors = CheckboxDefaults.colors(
                    checkedColor = selectedColor,
                    checkmarkColor = MaterialTheme.colorScheme.onSecondary
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                fontFamily = FontNunito.semiBoldBold(),
                fontSize = 15.sp,
                color = if (isSelected)
                    selectedColor
                else
                    MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
