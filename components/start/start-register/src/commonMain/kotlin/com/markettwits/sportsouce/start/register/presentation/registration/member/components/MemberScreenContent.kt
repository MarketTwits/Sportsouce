package com.markettwits.sportsouce.start.register.presentation.registration.member.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.textField.*
import com.markettwits.core_ui.items.screens.AdaptivePane
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
    AdaptivePane {
        Column(
            modifier = modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Участник ${userNumber + 1}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = FontNunito.bold()
                    )

                    MemberContactFace(
                        modifier = Modifier.padding(top = 8.dp),
                        checked = statement.contactPerson,
                        onValueChanged = {
                            onValueChanged(statement.copy(contactPerson = it))
                        }
                    )
                }
            }

            // Выбор существующего участника
            if (members.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Быстрый выбор",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            fontFamily = FontNunito.bold(),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        MemberSelectMember(
                            members = members,
                            selectedNameSurname = "${statement.surname} ${statement.name}",
                        ) {
                            onValueChanged(memberSelectApply(it, statement))
                        }
                    }
                }
            }

            // Основная информация
            OnBackgroundCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Основная информация",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        fontFamily = FontNunito.bold()
                    )

                    OutlinedTextFieldBase(
                        label = "Имя",
                        value = statement.name,
                        onValueChange = {
                            onValueChanged(statement.copy(name = it))
                        }
                    )

                    OutlinedTextFieldBase(
                        label = "Фамилия",
                        value = statement.surname,
                        onValueChange = {
                            onValueChanged(statement.copy(surname = it))
                        }
                    )

                    CalendarTextFiled(
                        textFiled = { calendarModifier ->
                            OutlinedTextFieldBase(
                                modifier = calendarModifier,
                                isEnabled = false,
                                label = "День рождения",
                                value = statement.birthday,
                                onValueChange = {
                                    onValueChanged(statement.copy(birthday = it))
                                }
                            )
                        },
                        onValueChanged = {
                            onValueChanged(statement.copy(birthday = it))
                        }
                    )

                    OutlinedTextFieldBase(
                        label = "Возраст",
                        isEnabled = false,
                        value = statement.age,
                        onValueChange = {}
                    )

                    DropDownSpinner(
                        selectedItem = statement.sex,
                        onItemSelected = { id, value ->
                            onValueChanged(statement.copy(sex = value))
                        },
                        itemList = statement.sexList.map { it.name }
                    ) {
                        OutlinedTextFieldBase(
                            label = "Пол",
                            value = statement.sex,
                            isEnabled = false
                        ) {}
                    }
                }
            }

            // Контактная информация
            if (statement.contactPerson) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Контактная информация",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            fontFamily = FontNunito.bold()
                        )

                        OutlinedTextFieldBase(
                            label = "Почта",
                            value = statement.email,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            onValueChange = {
                                onValueChanged(statement.copy(email = it))
                            }
                        )

                        OutlinePhoneTextFiled(
                            label = "Номер телефона",
                            value = statement.phone,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            onValueChange = {
                                onValueChanged(statement.copy(phone = it))
                            }
                        )
                    }
                }
            }

            // Дополнительная информация
            OnBackgroundCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Дополнительная информация",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        fontFamily = FontNunito.bold()
                    )

                    CityFiled(
                        statement = statement,
                        onValueChanged = onValueChanged::invoke
                    )

                    TeamFiled(
                        statement = statement,
                        onValueChanged = onValueChanged::invoke
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            MemberContinueButton(onClickContinue = {
                onClickContinue()
            })

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CityFiled(
    modifier: Modifier = Modifier,
    statement: StartStatement,
    onValueChanged: (StartStatement) -> Unit,
) {
    var cityChecked by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (cityChecked) {
            OutlinedTextFieldBase(
                label = "Город",
                isEnabled = false,
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
                }
            )
        }

        FilterPosition(
            item = "В списке нет моего города",
            checked = cityChecked,
            onClick = { cityChecked = !cityChecked }
        )
    }
}

@Composable
fun TeamFiled(
    modifier: Modifier = Modifier,
    statement: StartStatement,
    onValueChanged: (StartStatement) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
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
                }
            )
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
            }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Checkbox(
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.secondary,
                checkmarkColor = MaterialTheme.colorScheme.onSecondary,
                uncheckedColor = MaterialTheme.colorScheme.outline
            ),
            checked = checked,
            onCheckedChange = { onClick(item) }
        )
        Text(
            text = item,
            color = MaterialTheme.colorScheme.onPrimary,
            fontFamily = FontNunito.medium(),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}