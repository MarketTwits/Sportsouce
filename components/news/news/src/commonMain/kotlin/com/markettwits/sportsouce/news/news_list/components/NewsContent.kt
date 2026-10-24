package com.markettwits.sportsouce.news.news_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.markettwits.sportsouce.news.common.model.NewsItem

@Composable
fun NewsContent(
    modifier: Modifier = Modifier,
    items: List<NewsItem>,
    isLoading: Boolean = false,
    onClickItem: (NewsItem) -> Unit
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {
        val cardWidth = (maxWidth * 0.78f).coerceAtMost(480.dp)
        val showPlaceholders = isLoading && items.isEmpty()

        LazyRow(
            modifier = Modifier.fillMaxWidth().then(if (showPlaceholders) Modifier.semantics {
                contentDescription = "Загрузка новостей"
                progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
            } else Modifier),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (showPlaceholders) {
                items(3, key = { "loading-news-$it" }) {
                    NewsItemCardShimmer(cardWidth = cardWidth)
                }
            }
            items(items.take(10), key = { it.id }) { newsItem ->
                NewsItemCard(
                    modifier = Modifier,
                    newsInfo = newsItem,
                    cardWidth = cardWidth,
                    onClick = { onClickItem(newsItem) }
                )
            }
        }
    }
}
