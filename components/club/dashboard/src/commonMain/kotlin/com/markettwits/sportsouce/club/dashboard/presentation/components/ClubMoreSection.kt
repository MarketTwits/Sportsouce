package com.markettwits.sportsouce.club.dashboard.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.list.SauceActionRow
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
internal fun ClubMoreSection(onFaqClick: () -> Unit, onStatisticClick: () -> Unit) {
    OnBackgroundCard {
        Column(Modifier.padding(8.dp)) {
            SauceActionRow("Вопросы и ответы", onFaqClick, icon = Icons.AutoMirrored.Filled.HelpOutline)
            HorizontalDivider(Modifier.padding(horizontal = 8.dp), color = SauceTheme.colors.border)
            SauceActionRow("Клуб в цифрах", onStatisticClick, icon = Icons.AutoMirrored.Filled.ShowChart)
        }
    }
}
