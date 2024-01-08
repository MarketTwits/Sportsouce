package com.markettwits.news_list.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.failed_screen.FailedScreen
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.news_list.presentation.components.NewsItemCard
import com.markettwits.news_event.store.NewsEventStore
import com.markettwits.news_list.presentation.store.NewsStore


@Composable
fun NewsScreen(modifier: Modifier = Modifier, component: NewsComponent) {
    val state by component.value.collectAsState()
    Box(
        modifier = Modifier
    ) {
        if (state.isError) {
            FailedScreen(
                message = state.message,
                onClickRetry = {
                    component.obtainEvent(NewsStore.Intent.Launch)
                },
                onClickHelp = {
                }
            )
        }
        if (state.isLoading) {
            CircularProgressIndicator(color = SportSouceColor.SportSouceBlue)
        }
        LazyRow(
            modifier = Modifier
            .fillMaxWidth()
            .width(500.dp),
        ) {
            val itemsCount = state.news.take(10)
            items(itemsCount) {
                NewsItemCard(newsInfo = it) {
                    component.obtainEvent(NewsStore.Intent.OnClickItem(it))
                }
            }
        }
    }
}