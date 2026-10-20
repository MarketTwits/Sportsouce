package com.markettwits.sportsouce.start.presentation.series.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import com.markettwits.core_ui.items.components.navigation.SauceLazyColumn as LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import com.markettwits.core_ui.items.components.topbar.SauceScaffold as Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.topbar.TopBarBase
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

@Composable
internal fun StartSeriesContent(
    items: List<StartsListItem>,
    currentStartId: Int,
    onBack: () -> Unit,
    onClickStart: (StartsListItem) -> Unit,
) {
    Scaffold(
        containerColor = SauceTheme.colors.background,
        topBar = { TopBarBase(title = "Связанные старты", goBack = onBack) },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
        ) {
            itemsIndexed(items, key = { _, item -> item.id }) { index, item ->
                Column {
                    SeriesStartCard(
                        item = item,
                        isCurrentStart = item.id == currentStartId,
                        onClick = { if (item.id == currentStartId) onBack() else onClickStart(item) },
                    )
                    items.getOrNull(index + 1)?.let { next ->
                        StartSeriesConnector(toCurrentStart = next.id == currentStartId)
                    }
                }
            }
        }
    }
}

/** A connector has its own layout space; it cannot bleed through or cover a card. */
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
