package com.markettwits.sportsouce.auth.flow.internal.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
fun EnhancedEmailOrPhoneTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    isError: Boolean = false,
    errorMessage: String? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onValueChanged: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit = {},
) {
    Column(modifier = modifier) {
        OutlinedTextFieldBase(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    onFocusChanged(focusState.isFocused)
                },
            label = label,
            value = value,
            isError = isError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = keyboardActions,
            onValueChange = onValueChanged
        )

        if (isError && !errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                fontFamily = FontNunito.regular(),
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun EnhancedPasswordTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    isError: Boolean = false,
    errorMessage: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Done
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onValueChanged: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit = {},
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedTextFieldBase(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    onFocusChanged(focusState.isFocused)
                },
            label = label,
            value = value,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else
                    Icons.Filled.VisibilityOff
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, description, tint = MaterialTheme.colorScheme.tertiary)
                }
            },
            isError = isError,
            onValueChange = onValueChanged
        )

        if (isError && !errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                fontFamily = FontNunito.regular(),
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun EnhancedSmsCodeTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    isError: Boolean = false,
    errorMessage: String? = null,
    isSmsCodeSent: Boolean = false,
    smsCodeSending: Boolean = false,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onValueChanged: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit = {},
    onSendSmsClick: () -> Unit = {},
) {
    Column(modifier = modifier) {
        OutlinedTextFieldBase(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    onFocusChanged(focusState.isFocused)
                },
            label = label,
            value = value,
            isError = isError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = keyboardActions,
            trailingIcon = if (!isSmsCodeSent) {
                {
                    androidx.compose.material3.TextButton(
                        onClick = onSendSmsClick,
                        enabled = !smsCodeSending
                    ) {
                        Text(
                            text = if (smsCodeSending) "Отправка..." else "Отправить",
                            fontSize = 12.sp,
                            fontFamily = FontNunito.medium(),
                            color = if (smsCodeSending) {
                                MaterialTheme.colorScheme.outline
                            } else {
                                MaterialTheme.colorScheme.tertiary
                            }
                        )
                    }
                }
            } else null,
            onValueChange = { input ->
                // Only allow digits and limit to 6 characters
                val filtered = input.filter { it.isDigit() }.take(6)
                onValueChanged(filtered)
            }
        )

        if (isError && !errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                fontFamily = FontNunito.regular(),
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        if (isSmsCodeSent && !isError) {
            Text(
                text = "СМС код отправлен",
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 12.sp,
                fontFamily = FontNunito.regular(),
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}