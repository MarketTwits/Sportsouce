package com.markettwits.sportsouce.auth.flow.internal.sign_up.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.textField.CalendarTextFiled
import com.markettwits.core_ui.items.components.textField.DropDownSpinner
import com.markettwits.core_ui.items.components.textField.OutlinePhoneTextFiled
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.sportsouce.auth.flow.internal.common.OutlinePasswordTextField
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.model.SignUpFieldErrors
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.model.SignUpStage
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.model.SignUpStatement

@Composable
fun SignUpStages(
    modifier: Modifier = Modifier,
    signUpStage: SignUpStage,
    statement: SignUpStatement,
    focusManager: FocusManager,
    onValueChanged: (SignUpStatement) -> Unit,
    onNextStage: () -> Unit = {},
    onRegister: () -> Unit = {},
    errors: SignUpFieldErrors = SignUpFieldErrors(),
    validationAttempt: Int = 0,
    enabled: Boolean = true,
) {
    // Focus requesters for each stage
    val emailFocusRequester = remember { FocusRequester() }
    val surnameFocusRequester = remember { FocusRequester() }
    val repeatPasswordFocusRequester = remember { FocusRequester() }

    val fieldPositions = remember { List(8) { BringIntoViewRequester() } }
    LaunchedEffect(validationAttempt) {
        if (validationAttempt > 0) {
            val candidates = when (signUpStage) {
                SignUpStage.FIRST -> listOf(0 to errors.phone, 1 to errors.email)
                SignUpStage.SECOND -> listOf(2 to errors.name, 3 to errors.surname, 4 to errors.sex, 5 to errors.birthday)
                SignUpStage.THIRD -> listOf(6 to errors.password, 7 to errors.repeatPassword)
            }
            candidates.firstOrNull { it.second != null }?.let { fieldPositions[it.first].bringIntoView() }
        }
    }

    Column(
        modifier = modifier,
    ) {
        when (signUpStage) {
            SignUpStage.FIRST -> {
                OutlinePhoneTextFiled(
                    modifier = modifier.bringIntoViewRequester(fieldPositions[0]),
                    label = "Телефон",
                    value = statement.phone,
                    errorMessage = errors.phone,
                    isEnabled = enabled,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = { emailFocusRequester.requestFocus() }),
                ) {
                    onValueChanged(statement.copy(phone = it))
                }
                Spacer(Modifier.height(10.dp))
                OutlinedTextFieldBase(
                    modifier = modifier.focusRequester(emailFocusRequester).bringIntoViewRequester(fieldPositions[1]),
                    label = "Почта",
                    value = statement.email,
                    errorMessage = errors.email,
                    isEnabled = enabled,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            onNextStage()
                        }
                    )
                ) {
                    onValueChanged(statement.copy(email = it))
                }
            }

            SignUpStage.SECOND -> {
                OutlinedTextFieldBase(
                    modifier = modifier.bringIntoViewRequester(fieldPositions[2]),
                    label = "Имя",
                    value = statement.name,
                    errorMessage = errors.name,
                    isEnabled = enabled,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            surnameFocusRequester.requestFocus()
                        }
                    )
                ) {
                    onValueChanged(statement.copy(name = it))
                }
                Spacer(Modifier.height(10.dp))
                OutlinedTextFieldBase(
                    modifier = modifier.focusRequester(surnameFocusRequester).bringIntoViewRequester(fieldPositions[3]),
                    label = "Фамилия",
                    value = statement.surname,
                    errorMessage = errors.surname,
                    isEnabled = enabled,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            onNextStage()
                        }
                    )
                ) {
                    onValueChanged(statement.copy(surname = it))
                }

                val sexList = listOf("Мужской", "Женский")

                Spacer(Modifier.height(10.dp))

                DropDownSpinner(
                    itemList = sexList,
                    selectedItem = statement.sex,
                    enabled = enabled,
                    onItemSelected = { _, item ->
                        onValueChanged(statement.copy(sex = item))
                    },
                    textFiled = {
                        OutlinedTextFieldBase(
                            modifier = modifier.bringIntoViewRequester(fieldPositions[4]),
                            label = "Пол",
                            value = statement.sex,
                            errorMessage = errors.sex,
                            isEnabled = false
                        ) {}
                    }
                )

                Spacer(Modifier.height(10.dp))

                CalendarTextFiled(
                    modifier = modifier,
                    enabled = enabled,
                    textFiled = {
                        OutlinedTextFieldBase(
                            modifier = it.bringIntoViewRequester(fieldPositions[5]),
                            label = "Дата рождения",
                            value = statement.birthday,
                            errorMessage = errors.birthday,
                            isEnabled = false
                        ) {}
                    },
                    onValueChanged = {
                        onValueChanged(statement.copy(birthday = it))
                    }
                )
            }

            SignUpStage.THIRD -> {
                OutlinePasswordTextField(
                    modifier = modifier.bringIntoViewRequester(fieldPositions[6]),
                    label = "Пароль",
                    value = statement.password,
                    errorMessage = errors.password,
                    isEnabled = enabled,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            repeatPasswordFocusRequester.requestFocus()
                        }
                    )
                ) {
                    onValueChanged(statement.copy(password = it))
                }
                Spacer(Modifier.height(10.dp))
                OutlinePasswordTextField(
                    modifier = modifier.focusRequester(repeatPasswordFocusRequester).bringIntoViewRequester(fieldPositions[7]),
                    label = "Подтвердите пароль",
                    value = statement.repeatPassword,
                    errorMessage = errors.repeatPassword,
                    isEnabled = enabled,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            onRegister()
                        }
                    )
                ) {
                    onValueChanged(statement.copy(repeatPassword = it))
                }
            }
        }
    }
}