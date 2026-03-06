package com.markettwits.sportsouce.news.news_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.sportsouce.news.common.model.NewsItem

@Composable
fun NewsContent(
    modifier: Modifier = Modifier,
    items: List<NewsItem>,
    onClickItem: (NewsItem) -> Unit
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {
        val cardWidth = (maxWidth * 0.46f).coerceIn(240.dp, 360.dp)
        val cardHeight = (cardWidth * 0.88f).coerceIn(220.dp, 320.dp)

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(items.take(10), key = { it.id }) { newsItem ->
                NewsItemCard(
                    modifier = Modifier,
                    newsInfo = newsItem,
                    cardWidth = cardWidth,
                    cardHeight = cardHeight,
                    onClick = { onClickItem(newsItem) }
                )
            }
        }
    }
}
