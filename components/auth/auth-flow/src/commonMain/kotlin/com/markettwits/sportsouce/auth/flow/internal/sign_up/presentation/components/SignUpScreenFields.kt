package com.markettwits.sportsouce.auth.flow.internal.sign_up.presentation.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.markettwits.core_ui.items.components.textField.CalendarTextFiled
import com.markettwits.core_ui.items.components.textField.DropDownSpinner
import com.markettwits.core_ui.items.components.textField.OutlinePhoneTextFiled
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.sportsouce.auth.flow.internal.common.OutlinePasswordTextField
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.model.SignUpStatement

@Composable
internal fun SignUpScreenFields(
    modifier: Modifier = Modifier,
    statement: SignUpStatement,
    onValueChanged: (SignUpStatement) -> Unit
) {
    OutlinePhoneTextFiled(
        modifier = modifier,
        label = "Телефон",
        value = statement.phone,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Done
        ),
    ) {
        onValueChanged(statement.copy(phone = it))
    }
    OutlinedTextFieldBase(
        modifier = modifier,
        label = "Почта",
        value = statement.email,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    ) {
        onValueChanged(statement.copy(email = it))
    }

    OutlinedTextFieldBase(
        modifier = modifier,
        label = "Полное имя",
        value = statement.name,
    ) {
        onValueChanged(statement.copy(name = it))
    }
    OutlinedTextFieldBase(
        modifier = modifier,
        label = "Фамилия",
        value = statement.surname,
    ) {
        onValueChanged(statement.copy(surname = it))
    }

    val sexList = listOf("Мужской", "Женский")

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

    OutlinePasswordTextField(
        modifier = modifier,
        label = "Пароль",
        value = statement.password,
    ) {
        onValueChanged(statement.copy(password = it))
    }

    OutlinePasswordTextField(
        modifier = modifier,
        label = "Подтвердите пароль",
        value = statement.repeatPassword,
    ) {
        onValueChanged(statement.copy(repeatPassword = it))
    }
}
