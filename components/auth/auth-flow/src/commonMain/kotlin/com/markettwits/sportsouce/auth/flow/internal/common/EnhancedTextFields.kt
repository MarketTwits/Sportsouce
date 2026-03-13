package com.markettwits.sportsouce.auth.flow.internal.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
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
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.textField.OutlinePhoneTextFiled
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
fun EnhancedPhoneTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    isError: Boolean = false,
    errorMessage: String? = null,
    hintMessage: String? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onValueChanged: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit = {},
) {
    OutlinePhoneTextFiled(
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                onFocusChanged(focusState.isFocused)
            },
        label = label,
        value = value,
        isError = isError,
        keyboardActions = keyboardActions,
        supportingText = if (isError && !errorMessage.isNullOrBlank()) {
            {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    fontFamily = FontNunito.regular()
                )
            }
        } else if (!hintMessage.isNullOrBlank()) {
            {
                Text(
                    text = hintMessage,
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = 12.sp,
                    fontFamily = FontNunito.regular()
                )
            }
        } else null,
        onValueChange = onValueChanged
    )
}

@Composable
fun EnhancedEmailOrPhoneTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    isError: Boolean = false,
    errorMessage: String? = null,
    hintMessage: String? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onValueChanged: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit = {},
) {
    OutlinedTextFieldBase(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                contentType = ContentType.Username + ContentType.EmailAddress
            }
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
        supportingText = if (isError && !errorMessage.isNullOrBlank()) {
            {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    fontFamily = FontNunito.regular()
                )
            }
        } else if (!hintMessage.isNullOrBlank()) {
            {
                Text(
                    text = hintMessage,
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = 12.sp,
                    fontFamily = FontNunito.regular()
                )
            }
        } else null,
        onValueChange = onValueChanged
    )
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
                .semantics {
                    contentType = ContentType.Password
                }
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
                        if (smsCodeSending) {
                            androidx.compose.material3.CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        } else {
                            Text(
                                text = "Отправить",
                                fontSize = 12.sp,
                                fontFamily = FontNunito.medium(),
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                }
            } else {
                {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "SMS отправлен",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
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
