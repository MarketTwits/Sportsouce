package com.markettwits.sportsouce.start.presentation.series.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import com.markettwits.core_ui.items.components.navigation.SauceLazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.grid.itemsIndexed
import com.markettwits.core_ui.items.components.topbar.SauceScaffold as Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.topbar.TopBarBase
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.presentation.series.StartRelatedKind
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

@Composable
internal fun StartSeriesContent(
    items: List<StartsListItem>,
    currentStartId: Int,
    onBack: () -> Unit,
    onClickStart: (StartsListItem) -> Unit,
    kind: StartRelatedKind = StartRelatedKind.Series,
) {
    Scaffold(
        containerColor = SauceTheme.colors.background,
        topBar = { TopBarBase(title = kind.title, goBack = onBack) },
    ) { paddingValues ->
        Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.TopCenter) {
            SauceLazyVerticalGrid(
                columns = GridCells.Fixed(1),
                modifier = Modifier.widthIn(max = 720.dp).fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
            ) {
                itemsIndexed(items, key = { _, item -> item.id }) { index, item ->
                    Column {
                        val year = item.startDate?.take(4)?.toIntOrNull()
                        val previousYear = items.getOrNull(index - 1)?.startDate?.take(4)?.toIntOrNull()
                        val showYear = kind == StartRelatedKind.Archive && year != null && year != previousYear
                        if (index > 0) {
                            Box(Modifier.fillMaxWidth()) {
                                StartSeriesConnector(item.id == currentStartId, Modifier.matchParentSize())
                                Column(Modifier.fillMaxWidth()) {
                                    Spacer(Modifier.height(40.dp))
                                    if (showYear) ArchiveYearLabel(year!!)
                                }
                            }
                        } else if (showYear) ArchiveYearLabel(year!!)
                        SeriesStartCard(
                            item = item,
                            isCurrentStart = item.id == currentStartId,
                            onClick = { if (item.id == currentStartId) onBack() else onClickStart(item) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ArchiveYearLabel(year: Int) {
    Text(year.toString(), Modifier.padding(start = 8.dp, bottom = 12.dp),
        style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textSecondary)
}

/** Fills the gap between card edges, including the year label's measured height. */
@Composable
internal fun StartSeriesConnector(toCurrentStart: Boolean, modifier: Modifier = Modifier) {
    val color = if (toCurrentStart) SauceTheme.colors.brand else SauceTheme.colors.controlBorder
    Canvas(modifier.fillMaxWidth().height(40.dp)) {
        drawLine(
            color = color,
            start = Offset(size.width / 2, 0f), end = Offset(size.width / 2, size.height),
            strokeWidth = 2.dp.toPx(), cap = StrokeCap.Round,
            pathEffect = if (toCurrentStart) null else PathEffect.dashPathEffect(floatArrayOf(4.dp.toPx(), 6.dp.toPx())),
        )
    }
}
