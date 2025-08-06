package com.markettwits.sportsouce.auth.flow.internal.sign_up.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
) {
    // Focus requesters for each stage
    val emailFocusRequester = remember { FocusRequester() }
    val surnameFocusRequester = remember { FocusRequester() }
    val repeatPasswordFocusRequester = remember { FocusRequester() }

    Column(
        modifier = modifier,
    ) {
        when (signUpStage) {
            SignUpStage.FIRST -> {
                OutlinePhoneTextFiled(
                    modifier = modifier,
                    label = "Телефон",
                    value = statement.phone,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                ) {
                    onValueChanged(statement.copy(phone = it))
                }
                Spacer(Modifier.height(10.dp))
                OutlinedTextFieldBase(
                    modifier = modifier.focusRequester(emailFocusRequester),
                    label = "Почта",
                    value = statement.email,
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
                    modifier = modifier,
                    label = "Полное имя",
                    value = statement.name,
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
                    modifier = modifier.focusRequester(surnameFocusRequester),
                    label = "Фамилия",
                    value = statement.surname,
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
                    onItemSelected = { id, item ->
                        onValueChanged(statement.copy(sex = item))
                    },
                    textFiled = {
                        OutlinedTextFieldBase(
                            modifier = modifier,
                            label = "Пол",
                            value = statement.sex,
                            isEnabled = false
                        ) {}
                    }
                )

                Spacer(Modifier.height(10.dp))

                CalendarTextFiled(
                    modifier = modifier,
                    textFiled = {
                        OutlinedTextFieldBase(
                            modifier = it,
                            label = "Дата рождения",
                            value = statement.birthday,
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
                    modifier = modifier,
                    label = "Пароль",
                    value = statement.password,
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
                    modifier = modifier.focusRequester(repeatPasswordFocusRequester),
                    label = "Подтвердите пароль",
                    value = statement.repeatPassword,
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