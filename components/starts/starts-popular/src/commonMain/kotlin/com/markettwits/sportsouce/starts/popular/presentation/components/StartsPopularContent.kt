package com.markettwits.sportsouce.starts.popular.presentation.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.sportsouce.starts.common.presentation.StartCardV2


@Composable
internal fun StartsPopularContent(
    modifier: Modifier = Modifier,
    items: List<StartsListItem>,
    isRefreshing: Boolean = false,
    onClick: (StartsListItem) -> Unit,
    onRefresh: () -> Unit = {},
) {
    PullToRefreshScreen(
        modifier = modifier,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            modifier = modifier
        ) {
            item {
                StartsPopularInfo(Modifier.padding(10.dp))
            }
            itemsIndexed(items) { index: Int, item: StartsListItem ->
                Row(
                    modifier = Modifier.animateItem(fadeInSpec = tween(600)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            text = (index + 1).toString(),
                            color = MaterialTheme.colorScheme.tertiary,
                            fontFamily = FontNunito.bold(),
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                    }
                    Column {
                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            text = "Просмотры : ${item.views}",
                            color = MaterialTheme.colorScheme.tertiary,
                            fontFamily = FontNunito.bold(),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                        StartCardV2(
                            start = item,
                            onItemClick = { startId ->
                                onClick(startId)
                            }
                        )
                    }
                }
            }
        }
    }
}