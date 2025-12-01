package com.markettwits.sportsouce.start.presentation.series.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.topbar.TopBarBase
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

@Composable
internal fun StartSeriesContent(
    items: List<StartsListItem>,
    currentStartId: Int,
    onBack: () -> Unit,
    onClickStart: (StartsListItem) -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            TopBarBase(
                title = "Связанные старты",
                goBack = onBack
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = paddingValues.calculateTopPadding()),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            item {
                Spacer(Modifier.height(8.dp))
            }
            itemsIndexed(items) { index, item ->
                SeriesStartCard(
                    modifier = Modifier.fillMaxWidth(),
                    item = item,
                    isCurrentStart = item.id == currentStartId,
                    isLast = index == items.lastIndex,
                    onClick = {
                        if (item.id == currentStartId) {
                            onBack()
                        } else {
                            onClickStart(item)
                        }
                    }
                )
            }
            item {
                Spacer(
                    Modifier
                        .height(8.dp)
                        .windowInsetsPadding(WindowInsets.statusBars)
                )
            }
        }
    }
}
