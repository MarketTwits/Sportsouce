package com.markettwits.news_list.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.news_list.domain.NewsInfo

@Composable
fun NewsContent(
    modifier: Modifier = Modifier,
    items: List<NewsInfo>,
    onClickItem: (NewsInfo) -> Unit
) {
    Box(
        modifier = Modifier
    ) {
        LazyRow(
            modifier = modifier
                .fillMaxWidth()
                .width(500.dp),
        ) {
            items(items.take(10)) {
                NewsItemCard(newsInfo = it) {
                    onClickItem(it)
                }
            }
        }
    }
}