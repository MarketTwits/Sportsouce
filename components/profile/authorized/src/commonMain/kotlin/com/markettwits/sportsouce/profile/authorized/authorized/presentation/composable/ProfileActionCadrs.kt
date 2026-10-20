package com.markettwits.sportsouce.profile.authorized.authorized.presentation.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.list.SauceActionRow
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
internal fun ProfileActionCadrs(
    modifier: Modifier = Modifier,
    onClickClub: () -> Unit,
    onClickHelp: () -> Unit,
    onClickSettings: () -> Unit,
) {
    OnBackgroundCard(modifier) {
        Column(Modifier.padding(8.dp)) {
            SauceActionRow("Клуб", onClickClub, icon = Icons.Default.Groups)
            HorizontalDivider(color = SauceTheme.colors.border, modifier = Modifier.padding(horizontal = 8.dp))
            SauceActionRow("Настройки", onClickSettings, icon = Icons.Default.Settings)
            HorizontalDivider(color = SauceTheme.colors.border, modifier = Modifier.padding(horizontal = 8.dp))
            SauceActionRow("Помощь", onClickHelp, icon = Icons.AutoMirrored.Filled.Message)
        }
    }
}
