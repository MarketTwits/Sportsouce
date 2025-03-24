package com.markettwits.news.news_list.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.news.common.model.NewsItem

@Composable
fun NewsContent(
    modifier: Modifier = Modifier,
    items: List<NewsItem>,
    onClickItem: (NewsItem) -> Unit
) {
    Text(
        modifier = modifier.padding(horizontal = 10.dp),
        text = "Последние новости",
        color = MaterialTheme.colorScheme.tertiary,
        fontFamily = FontNunito.bold(),
        fontSize = 18.sp
    )
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