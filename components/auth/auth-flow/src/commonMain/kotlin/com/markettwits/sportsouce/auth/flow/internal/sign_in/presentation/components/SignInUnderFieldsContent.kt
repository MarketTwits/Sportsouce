package com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.sportsouce.auth.flow.internal.common.AuthButton

@Composable
fun SignInUnderFieldsContent(
    modifier: Modifier = Modifier,
    onClickRegistry: () -> Unit,
    onClickAuth: () -> Unit,
    isButtonEnabled: Boolean,
    isButtonLoading: Boolean,
) {
    Column(modifier.fillMaxWidth().padding(top = SauceSpacing.small, bottom = SauceSpacing.medium),
        verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
        SauceButton(
            title = "Забыли пароль?",
            onClick = onClickRegistry,
            modifier = Modifier.align(Alignment.End),
            variant = SauceButtonVariant.Text,
            fillWidth = false,
        )
        AuthButton(
            title = "Войти",
            enabled = isButtonEnabled,
            loading = isButtonLoading,
            onClick = onClickAuth,
        )
    }
}
