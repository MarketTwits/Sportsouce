package com.markettwits.profile.internal.forgot_password.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.profile.internal.common.AuthButton
import com.markettwits.profile.internal.common.ConsumeRowContent
import com.markettwits.profile.internal.common.WelcomeContent

@Composable
internal fun ForgotPasswordContent(
    modifier: Modifier = Modifier,
    email: String,
    isLoading: Boolean,
    onValueChanged: (String) -> Unit,
    onClickConsume: () -> Unit,
    onClickRecover: () -> Unit
) {
    val focus = LocalFocusManager.current
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WelcomeContent("Введите вашу почту и мы вышлем вам ссылку для восстановления пароля")
        OutlinedTextFieldBase(
            modifier = modifier,
            label = "Почта",
            value = email,
        ) {
            onValueChanged(it)
        }
        AuthButton(
            modifier = modifier,
            "Восстановить",
            enabled = !isLoading && email.isNotEmpty(),
            loading = isLoading
        ) {
            focus.clearFocus()
            onClickRecover()
        }
        ConsumeRowContent(title = "Назад", onClickConsume = onClickConsume)
    }
}