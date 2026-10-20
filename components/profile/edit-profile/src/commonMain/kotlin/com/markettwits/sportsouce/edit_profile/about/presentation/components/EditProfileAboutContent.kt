package com.markettwits.sportsouce.edit_profile.about.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
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
    OnBackgroundCard {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Обо мне", style = MaterialTheme.typography.titleMedium,
                color = SauceTheme.colors.textPrimary)
            OutlinedTextFieldBase(
                label = "Статус", isError = state.fieldStat.isError,
                value = state.fieldStat.value, maxLines = 6, minLines = 4,
                singleLine = false, onValueChange = onValueChanged,
            )
            FlowRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End),
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (state.message.isNotBlank()) Text(state.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (state.isError) SauceTheme.colors.danger.content else SauceTheme.colors.textSecondary)
                Text("${state.fieldStat.value.length} / 120", style = MaterialTheme.typography.bodySmall,
                    color = if (state.fieldStat.isError) SauceTheme.colors.danger.content else SauceTheme.colors.textMuted)
            }
            SauceButton("Сохранить", apply, enabled = !state.fieldStat.isError, loading = state.isLoading)
            SauceButton("Отмена", dismiss, variant = SauceButtonVariant.Outlined)
        }
    }
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            delay(800)
            dismiss()
        }
    }
}
