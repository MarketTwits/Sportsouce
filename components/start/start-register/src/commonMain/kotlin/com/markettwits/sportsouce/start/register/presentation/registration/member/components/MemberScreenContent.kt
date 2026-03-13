package com.markettwits.sportsouce.start.register.presentation.registration.member.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.textField.*
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import com.markettwits.sportsouce.start.register.domain.StartStatement
import com.markettwits.sportsouce.start.register.presentation.registration.member.domain.isValidEmail
import com.markettwits.sportsouce.start.register.presentation.registration.member.domain.isValidPhone

@Composable
fun MemberScreenContent(
    modifier: Modifier = Modifier,
    userNumber: Int,
    statement: StartStatement,
    members: List<ProfileMember>,
    onValueChanged: (StartStatement) -> Unit,
    isAddToProfileEnabled: Boolean,
    onClickAddToProfile: () -> Unit,
) {
    var selectedExistingMemberLabel by rememberSaveable(userNumber) {
        mutableStateOf(
            members.firstOrNull {
                it.name == statement.name && it.surname == statement.surname
            }?.let { "${it.surname} ${it.name}" } ?: ""
        )
    }
    LaunchedEffect(members) {
        val actualMember = members.firstOrNull {
            it.name == statement.name &&
                    it.surname == statement.surname &&
                    it.birthday == statement.birthday
        }
        if (actualMember != null) {
            selectedExistingMemberLabel = "${actualMember.surname} ${actualMember.name}"
        }
    }
    val nameValidation = validateName(statement.name)
    val surnameValidation = validateSurname(statement.surname)
    val birthdayValidation = validateBirthday(statement.birthday)
    val ageValidation = validateAge(statement.age)
    val sexValidation = validateSex(statement.sex)
    val cityValidation = validateCity(statement.city)
    val teamValidation = validateTeam(statement.team)
    val emailValidation = validateEmail(statement.email, statement.contactPerson)
    val phoneValidation = validatePhone(statement.phone, statement.contactPerson)
    AdaptivePane {
        Column(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Участник ${userNumber + 1}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = FontNunito.bold()
                    )

                    MemberContactFace(
                        modifier = Modifier.padding(start = 12.dp),
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
                        containerColor = MaterialTheme.colorScheme.primary
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
                            selectedNameSurname = selectedExistingMemberLabel,
                        ) { member ->
                            selectedExistingMemberLabel = "${member.surname} ${member.name}"
                            onValueChanged(memberSelectApply(member, statement))
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedButton(
                            enabled = isAddToProfileEnabled,
                            onClick = onClickAddToProfile,
                            modifier = Modifier.fillMaxWidth(),
                            border = ButtonDefaults.outlinedButtonBorder
                        ) {
                            val contentColor = if (isAddToProfileEnabled) {
                                MaterialTheme.colorScheme.tertiary
                            } else {
                                MaterialTheme.colorScheme.outline
                            }
                            Icon(
                                imageVector = Icons.Default.PersonAdd,
                                contentDescription = null,
                                tint = contentColor
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Добавить в профиль",
                                color = contentColor,
                                fontFamily = FontNunito.semiBoldBold()
                            )
                        }
                    }
                }
            }

            // Основная информация
            OnBackgroundCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
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
                        isError = nameValidation.isError,
                        supportingText = {
                            FieldSupportingText(nameValidation)
                        },
                        leadingIcon = {
                            FieldLeadingIcon(Icons.Default.Person)
                        },
                        trailingIcon = {
                            FieldStateIcon(nameValidation)
                        },
                        onValueChange = {
                            onValueChanged(statement.copy(name = it))
                        }
                    )

                    OutlinedTextFieldBase(
                        label = "Фамилия",
                        value = statement.surname,
                        isError = surnameValidation.isError,
                        supportingText = {
                            FieldSupportingText(surnameValidation)
                        },
                        leadingIcon = {
                            FieldLeadingIcon(Icons.Default.Person)
                        },
                        trailingIcon = {
                            FieldStateIcon(surnameValidation)
                        },
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
                                isError = birthdayValidation.isError,
                                supportingText = {
                                    FieldSupportingText(birthdayValidation)
                                },
                                leadingIcon = {
                                    FieldLeadingIcon(Icons.Default.CalendarMonth)
                                },
                                trailingIcon = {
                                    FieldStateIcon(birthdayValidation)
                                },
                                onValueChange = {
                                    onValueChanged(statement.copy(birthday = it))
                                }
                            )
                        },
                        onValueChanged = {
                            onValueChanged(statement.copy(birthday = it))
                        }
                    )

                    CalendarTextFiled(
                        textFiled = { calendarModifier ->
                            OutlinedTextFieldBase(
                                modifier = calendarModifier,
                                label = "Возраст",
                                isEnabled = false,
                                value = statement.age,
                                isError = ageValidation.isError,
                                supportingText = {
                                    FieldSupportingText(ageValidation)
                                },
                                leadingIcon = {
                                    FieldLeadingIcon(Icons.Default.CalendarMonth)
                                },
                                trailingIcon = {
                                    FieldStateIcon(ageValidation)
                                },
                                onValueChange = {}
                            )
                        },
                        onValueChanged = {
                            onValueChanged(statement.copy(birthday = it))
                        }
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
                            isEnabled = false,
                            isError = sexValidation.isError,
                            supportingText = {
                                FieldSupportingText(sexValidation)
                            },
                            leadingIcon = {
                                FieldLeadingIcon(Icons.Default.Person)
                            }
                        ) {}
                    }
                }
            }

            // Контактная информация
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
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

                    Text(
                        text = if (statement.contactPerson)
                            "Для контактного участника поля обязательны"
                        else
                            "Для неконтактного участника поля необязательны",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                        fontFamily = FontNunito.medium()
                    )

                    OutlinedTextFieldBase(
                        label = "Почта",
                        value = statement.email,
                        isError = emailValidation.isError,
                        supportingText = {
                            FieldSupportingText(emailValidation)
                        },
                        leadingIcon = {
                            FieldLeadingIcon(Icons.Default.Email)
                        },
                        trailingIcon = {
                            FieldStateIcon(emailValidation)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        onValueChange = {
                            onValueChanged(statement.copy(email = it))
                        }
                    )

                    OutlinePhoneTextFiled(
                        label = "Номер телефона",
                        value = statement.phone,
                        isError = phoneValidation.isError,
                        supportingText = {
                            FieldSupportingText(phoneValidation)
                        },
                        leadingIcon = {
                            FieldLeadingIcon(Icons.Default.Phone)
                        },
                        trailingIcon = {
                            FieldStateIcon(phoneValidation)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        onValueChange = {
                            onValueChanged(statement.copy(phone = it))
                        }
                    )
                }
            }

            // Дополнительная информация
            OnBackgroundCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
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
                        validation = cityValidation,
                        onValueChanged = onValueChanged::invoke
                    )

                    TeamFiled(
                        statement = statement,
                        validation = teamValidation,
                        onValueChanged = onValueChanged::invoke
                    )
                }
            }

            Spacer(modifier = Modifier.height(96.dp))
        }
    }
}

@Composable
private fun CityFiled(
    modifier: Modifier = Modifier,
    statement: StartStatement,
    validation: FieldValidation,
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
                isError = validation.isError,
                supportingText = {
                    FieldSupportingText(validation)
                },
                leadingIcon = {
                    FieldLeadingIcon(Icons.Default.LocationOn)
                },
                trailingIcon = {
                    FieldStateIcon(validation)
                },
                onValueChange = {
                    onValueChanged(statement.copy(city = it))
                }
            )
        } else {
            ItemsTextFiledDialog(
                label = "Город",
                value = statement.city,
                items = statement.cities.map { it.name },
                leadingIcon = {
                    FieldLeadingIcon(Icons.Default.LocationOn)
                },
                onValueChanged = {
                    onValueChanged(statement.copy(city = it))
                }
            )
            FieldSupportingText(validation)
        }

        FilterPosition(
            item = "В списке нет моего города",
            checked = cityChecked,
            onClick = { cityChecked = !cityChecked }
        )
    }
}

@Composable
private fun TeamFiled(
    modifier: Modifier = Modifier,
    statement: StartStatement,
    validation: FieldValidation,
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
                isError = validation.isError,
                supportingText = {
                    FieldSupportingText(validation)
                },
                leadingIcon = {
                    FieldLeadingIcon(Icons.Default.Groups)
                },
                trailingIcon = {
                    FieldStateIcon(validation)
                },
                onValueChange = {
                    onValueChanged(statement.copy(team = it))
                }
            )
        } else {
            ItemsTextFiledDialog(
                label = "Команда",
                value = statement.team,
                items = statement.teams.map { it.name },
                leadingIcon = {
                    FieldLeadingIcon(Icons.Default.Groups)
                },
                onValueChanged = {
                    onValueChanged(statement.copy(team = it))
                }
            )
            FieldSupportingText(validation)
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

private data class FieldValidation(
    val isError: Boolean,
    val message: String,
)

@Composable
private fun FieldSupportingText(validation: FieldValidation) {
    Text(
        text = validation.message,
        color = if (validation.isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
        style = MaterialTheme.typography.bodySmall,
        fontFamily = FontNunito.medium()
    )
}

@Composable
private fun FieldStateIcon(validation: FieldValidation) {
    Icon(
        imageVector = if (validation.isError) Icons.Default.ErrorOutline else Icons.Default.CheckCircle,
        contentDescription = null,
        tint = if (validation.isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
    )
}

@Composable
private fun FieldLeadingIcon(imageVector: ImageVector) {
    Icon(
        imageVector = imageVector,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.tertiary
    )
}

private fun validateName(value: String): FieldValidation {
    val clean = value.trim()
    val nameRegex = Regex("^[А-Яа-яЁё]+$")
    return when {
        clean.isEmpty() -> FieldValidation(true, "Заполните имя")
        clean.length < 2 -> FieldValidation(true, "Минимум 2 символа")
        !clean.matches(nameRegex) -> FieldValidation(true, "Только русские буквы")
        else -> FieldValidation(false, "Имя заполнено")
    }
}

private fun validateSurname(value: String): FieldValidation {
    val clean = value.trim()
    val surnameRegex = Regex("^[А-Яа-яЁё]+$")
    return when {
        clean.isEmpty() -> FieldValidation(true, "Заполните фамилию")
        clean.length < 2 -> FieldValidation(true, "Минимум 2 символа")
        !clean.matches(surnameRegex) -> FieldValidation(true, "Только русские буквы")
        else -> FieldValidation(false, "Фамилия заполнена")
    }
}

private fun validateBirthday(value: String): FieldValidation {
    val clean = value.trim()
    val dateRegex = Regex("^\\d{2}\\.\\d{2}\\.\\d{4}$")
    return when {
        clean.isEmpty() -> FieldValidation(true, "Укажите дату рождения")
        !clean.matches(dateRegex) -> FieldValidation(true, "Формат: ДД.ММ.ГГГГ")
        else -> FieldValidation(false, "Дата заполнена")
    }
}

private fun validateSex(value: String): FieldValidation {
    return if (value.trim().isEmpty()) {
        FieldValidation(true, "Выберите пол")
    } else {
        FieldValidation(false, "Пол выбран")
    }
}

private fun validateAge(value: String): FieldValidation {
    val age = value.toIntOrNull()
    return when {
        value.isBlank() -> FieldValidation(true, "Возраст не рассчитан")
        age == null -> FieldValidation(true, "Некорректный возраст")
        age < 1 -> FieldValidation(true, "Возраст должен быть не меньше 1 года")
        else -> FieldValidation(false, "Возраст корректный")
    }
}

private fun validateCity(value: String): FieldValidation {
    val clean = value.trim()
    return when {
        clean.isEmpty() -> FieldValidation(true, "Выберите город")
        clean.length < 3 -> FieldValidation(true, "Минимум 3 символа")
        else -> FieldValidation(false, "Город выбран")
    }
}

private fun validateTeam(value: String): FieldValidation {
    return if (value.trim().isEmpty()) {
        FieldValidation(true, "Выберите команду")
    } else {
        FieldValidation(false, "Команда выбрана")
    }
}

private fun validateEmail(value: String, isContactPerson: Boolean): FieldValidation {
    val clean = value.trim()
    return when {
        clean.isEmpty() && isContactPerson -> FieldValidation(true, "Для контактного участника email обязателен")
        clean.isEmpty() -> FieldValidation(false, "Поле необязательное")
        !clean.isValidEmail() -> FieldValidation(true, "Введите корректный email")
        else -> FieldValidation(false, "Email корректный")
    }
}

private fun validatePhone(value: String, isContactPerson: Boolean): FieldValidation {
    val clean = value.trim()
    return when {
        clean.isEmpty() && isContactPerson -> FieldValidation(true, "Для контактного участника телефон обязателен")
        clean.isEmpty() -> FieldValidation(false, "Поле необязательное")
        !clean.isValidPhone() -> FieldValidation(true, "Введите корректный номер телефона")
        else -> FieldValidation(false, "Телефон корректный")
    }
}

internal fun isMemberFormValid(
    statement: StartStatement,
    contactPerson: Boolean,
): Boolean {
    return listOf(
        validateName(statement.name),
        validateSurname(statement.surname),
        validateBirthday(statement.birthday),
        validateAge(statement.age),
        validateSex(statement.sex),
        validateCity(statement.city),
        validateTeam(statement.team),
        validateEmail(statement.email, contactPerson),
        validatePhone(statement.phone, contactPerson)
    ).none { it.isError }
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
