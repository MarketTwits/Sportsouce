package com.markettwits.selfupdater.components.selft_update.components.update_available

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.dialog.SauceDialog
import com.markettwits.core_ui.items.components.dialog.SauceDialogContent
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
fun SelfUpdateStartedDialog(message: String, result: Boolean, onDismiss: () -> Unit) {
    SauceDialog(onDismissRequest = onDismiss) {
        SauceDialogContent(
            title = if (result) "Обновление" else "Не удалось обновить",
            actions = { SauceButton("Понятно", onDismiss) },
        ) {
            Text(message, color = SauceTheme.colors.textSecondary, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
