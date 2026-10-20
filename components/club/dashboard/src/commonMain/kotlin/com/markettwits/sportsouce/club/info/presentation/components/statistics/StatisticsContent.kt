package com.markettwits.sportsouce.club.info.presentation.components.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.navigation.SauceLazyColumn
import androidx.compose.foundation.lazy.items
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.club.common.presentation.*
import com.markettwits.sportsouce.club.info.domain.models.Statistic

@Composable
internal fun StatisticContents(modifier: Modifier = Modifier, statistics: List<Statistic>) {
    SauceLazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (statistics.isEmpty()) item { ClubNotice("Клуб в цифрах", "Информация появится здесь позже.") }
        items(statistics, key = { it.id }) { statistic ->
            OnBackgroundCard {
                Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(statistic.value, style = MaterialTheme.typography.headlineMedium, color = SauceTheme.colors.textPrimary)
                    HtmlText(text = statistic.title, style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary, showImages = false)
                }
            }
        }
    }
}
