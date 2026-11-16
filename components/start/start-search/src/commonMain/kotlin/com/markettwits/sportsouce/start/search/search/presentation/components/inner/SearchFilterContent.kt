package com.markettwits.sportsouce.start.search.search.presentation.components.inner

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.list.SauceActionRow
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.search.filter.presentation.component.StartFilterUi
import com.markettwits.sportsouce.start.search.filter.presentation.components.filterDateLabel

@Composable
internal fun SearchFilterContent(
    filter: StartFilterUi,
    onClickPanel: () -> Unit,
    onClickRemoveFilter: () -> Unit,
) {
    OnBackgroundCard(Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SauceActionRow(
                title = "Фильтры",
                subtitle = filter.selectedSummary(),
                subtitleMaxLines = 2,
                count = filter.activeCount.toString(),
                icon = Icons.Outlined.Tune,
                modifier = Modifier.weight(1f),
                onClick = onClickPanel,
            )
            IconButton(onClick = onClickRemoveFilter, modifier = Modifier.clip(SauceShapes.button)) {
                Icon(Icons.Default.Close, contentDescription = "Сбросить фильтры", tint = SauceTheme.colors.textSecondary)
            }
        }
    }
}

private fun StartFilterUi.selectedSummary(): String {
    val from = fromDate?.filterDateLabel()
    val to = toDate?.filterDateLabel()
    val period = when {
        from != null && to != null -> if (from == to) from else "$from – $to"
        from != null -> "С $from"
        to != null -> "По $to"
        else -> null
    }
    return (listOfNotNull(period) + items.flatMap { it.selected }).joinToString(" · ")
}
