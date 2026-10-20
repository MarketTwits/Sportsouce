package com.markettwits.sportsouce.edit_profile.sign_out.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
fun EditProfileSignOutContent(
    modifier: Modifier = Modifier,
    dismiss: () -> Unit,
    apply: () -> Unit,
) {
    OnBackgroundCard(modifier) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Выйти из аккаунта?", style = MaterialTheme.typography.titleMedium,
                color = SauceTheme.colors.textPrimary)
            SauceButton("Выйти", apply, variant = SauceButtonVariant.Destructive)
            SauceButton("Отмена", dismiss, variant = SauceButtonVariant.Outlined)
        }
    }
}
