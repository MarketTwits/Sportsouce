package com.markettwits.sportsouce.profile.members.members_list.presentation.components.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PeopleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
fun MembersEmptyCard(modifier: Modifier = Modifier) {
    OnBackgroundCard(modifier) {
        Column(Modifier.fillMaxWidth().padding(SauceSpacing.extraLarge),
            verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
            Icon(Icons.Default.PeopleOutline, null, tint = SauceTheme.colors.textMuted)
            Text("У вас ещё нет участников", style = MaterialTheme.typography.titleMedium,
                color = SauceTheme.colors.textPrimary)
            Text("Добавьте данные близких или команды, чтобы использовать их при регистрации на старты.",
                style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
        }
    }
}
