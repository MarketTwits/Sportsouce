package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.list.SauceActionRow
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

@Composable
internal fun StartArchivePanel(
    modifier: Modifier = Modifier,
    items: List<StartsListItem>,
    currentStartId: Int,
    loading: Boolean,
    failed: Boolean,
    onClickArchive: () -> Unit,
    onRetry: () -> Unit,
    onClickStart: (StartsListItem) -> Unit,
) {
    when {
        items.isNotEmpty() -> StartSeriesPanel(
            modifier, items, currentStartId, onClickArchive, onClickStart,
            title = "Архив стартов",
            previewItems = items.filter { it.id != currentStartId }.takeLast(3),
        )
        loading -> Column(modifier.fillMaxWidth().semantics { contentDescription = "Загрузка архива стартов" },
            verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
            SauceSkeleton(Modifier.width(160.dp).height(24.dp))
            SauceSkeleton(Modifier.fillMaxWidth().height(120.dp), SauceShapes.card)
        }
        failed -> SauceActionRow(
            title = "Архив стартов", subtitle = "Не удалось загрузить. Нажмите, чтобы повторить", onClick = onRetry, modifier = modifier,
        )
    }
}
