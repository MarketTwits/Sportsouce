package com.markettwits.sportsouce.club.info.presentation.components.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.navigation.SauceLazyColumn
import com.markettwits.sportsouce.club.common.presentation.ClubMetricsRow
import com.markettwits.sportsouce.club.common.presentation.ClubNotice
import com.markettwits.sportsouce.club.info.domain.models.Statistic

@Composable
internal fun StatisticContents(modifier: Modifier = Modifier, statistics: List<Statistic>) {
    BoxWithConstraints(modifier.fillMaxWidth()) {
        val columns = if (maxWidth >= 320.dp * LocalDensity.current.fontScale) 2 else 1
        val rows = remember(statistics, columns) { statistics.chunked(columns) }
        SauceLazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            if (statistics.isEmpty()) {
                item { ClubNotice("Данных пока нет", "Информация появится здесь позже.") }
            }
            items(rows, key = { it.first().id }) { metrics ->
                ClubMetricsRow(metrics, Modifier.heightIn(min = 116.dp))
            }
        }
    }
}
