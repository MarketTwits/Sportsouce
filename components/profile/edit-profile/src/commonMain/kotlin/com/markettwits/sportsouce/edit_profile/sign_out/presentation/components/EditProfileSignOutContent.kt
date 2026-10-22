package com.markettwits.sportsouce.edit_profile.sign_out.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.dialog.SauceDialogContent
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
fun EditProfileSignOutContent(
    modifier: Modifier = Modifier,
    dismiss: () -> Unit,
    apply: () -> Unit,
) {
    SauceDialogContent(
        title = "Выйти из аккаунта?",
        modifier = modifier,
        actions = {
            SauceButton("Выйти", apply, variant = SauceButtonVariant.Destructive)
            SauceButton("Отмена", dismiss, variant = SauceButtonVariant.Text)
        },
    ) {
        Text("Чтобы снова открыть свой профиль, потребуется войти в аккаунт.",
            style = MaterialTheme.typography.bodyLarge, color = SauceTheme.colors.textSecondary)
    }
}
