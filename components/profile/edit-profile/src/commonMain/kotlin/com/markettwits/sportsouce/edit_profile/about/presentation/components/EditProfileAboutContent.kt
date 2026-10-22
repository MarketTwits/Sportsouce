package com.markettwits.sportsouce.edit_profile.about.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.dialog.SauceDialogContent
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.edit_profile.about.presentation.store.EditProfileAboutStore
import kotlinx.coroutines.delay

@Composable
fun EditProfileAboutContent(
    state: EditProfileAboutStore.State,
    dismiss: () -> Unit,
    apply: () -> Unit,
    onValueChanged: (String) -> Unit,
) {
    SauceDialogContent(
        title = "Обо мне",
        actions = {
            SauceButton("Сохранить", apply, enabled = !state.fieldStat.isError, loading = state.isLoading)
            SauceButton("Отмена", dismiss, variant = SauceButtonVariant.Text)
        },
    ) {
        OutlinedTextFieldBase(
            label = "Статус", isError = state.fieldStat.isError,
            value = state.fieldStat.value, maxLines = 6, minLines = 4,
            singleLine = false, onValueChange = onValueChanged,
        )
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(state.message, Modifier.weight(1f), style = MaterialTheme.typography.bodySmall,
                color = if (state.isError) SauceTheme.colors.danger.content else SauceTheme.colors.textSecondary)
            Text("${state.fieldStat.value.length} / 120", style = MaterialTheme.typography.bodySmall,
                color = if (state.fieldStat.isError) SauceTheme.colors.danger.content else SauceTheme.colors.textMuted)
        }
    }
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            delay(800)
            dismiss()
        }
    }
}
