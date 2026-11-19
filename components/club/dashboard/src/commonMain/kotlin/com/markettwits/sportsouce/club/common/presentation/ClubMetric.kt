package com.markettwits.sportsouce.club.common.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.club.info.domain.models.Statistic

@Composable
internal fun ClubMetricsRow(statistics: List<Statistic>, modifier: Modifier = Modifier, compact: Boolean = false) {
    Row(
        modifier.fillMaxWidth().height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(if (compact) 8.dp else 10.dp),
    ) {
        statistics.forEach { statistic ->
            ClubMetric(statistic, Modifier.weight(1f).fillMaxHeight(), compact)
        }
    }
}

@Composable
private fun ClubMetric(statistic: Statistic, modifier: Modifier, compact: Boolean) {
    val colors = SauceTheme.colors
    val typography = MaterialTheme.typography
    Surface(modifier, shape = SauceShapes.field, color = if (compact) colors.surfaceSubtle else colors.actionSoft) {
        Column(
            Modifier.fillMaxWidth().padding(if (compact) 12.dp else 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                statistic.value,
                style = (if (compact) typography.headlineSmall else typography.headlineMedium).copy(fontFeatureSettings = "tnum"),
                fontWeight = FontWeight.SemiBold,
                color = colors.action,
            )
            HtmlText(
                text = statistic.title,
                style = if (compact) typography.bodySmall else typography.bodyMedium,
                color = colors.textSecondary,
                showImages = false,
            )
        }
    }
}
