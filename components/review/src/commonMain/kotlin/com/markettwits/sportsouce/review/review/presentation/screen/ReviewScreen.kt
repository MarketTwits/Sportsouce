package com.markettwits.sportsouce.review.review.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.toolbar.CollapsingToolbarScaffold
import com.markettwits.core_ui.items.components.toolbar.ScrollStrategy
import com.markettwits.core_ui.items.components.toolbar.rememberCollapsingToolbarScaffoldState
import com.markettwits.core_ui.items.screens.FailedScreen
import com.markettwits.core_ui.items.screens.LoadingFullScreen
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.sportsouce.review.review.presentation.component.ReviewComponent
import com.markettwits.sportsouce.review.review.presentation.components.content.ReviewContent
import com.markettwits.sportsouce.review.review.presentation.store.ReviewStore
import com.markettwits.sportsouce.start.search.search.presentation.components.publish.StartsSearchBarPublic

@Composable
fun ReviewScreen(
    component: ReviewComponent,
    notification: @Composable ((Modifier) -> Unit),
) {
    val state by component.value.collectAsState()
    CollapsingToolbarScaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.primary),
        state = rememberCollapsingToolbarScaffoldState(),
        scrollStrategy = ScrollStrategy.EnterAlways,
        toolbar = {
            StartsSearchBarPublic(
                onClickSearchPanel = {
                    component.obtainEvent(ReviewStore.Intent.OnClickSearch)
                }, onClickSettings = {
                    component.obtainEvent(ReviewStore.Intent.OnClickSettings)
                })
        }
    ) {
        PullToRefreshScreen(
            isRefreshing = state.review.actualStarts.isNotEmpty() && state.isLoading,
            onRefresh = {
                component.obtainEvent(ReviewStore.Intent.Launch(true))
            }) {
            Column(
                modifier = it
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                ReviewContent(
                    news = state.review.news,
                    actual = state.review.actualStarts,
                    archive = state.review.archiveStarts,
                    notification = notification,
                    onClickStart = {
                        component.obtainEvent(ReviewStore.Intent.OnClickItem(it))
                    },
                    onClickNewsInfo = {
                        component.obtainEvent(ReviewStore.Intent.OnClickNews(it))
                    },
                    onClickMenu = {
                        component.obtainEvent(ReviewStore.Intent.OnClickMenu(it))
                    },
                    onClickTelegram = {
                        component.obtainEvent(ReviewStore.Intent.OnClickTelegram)
                    },
                    onClickVk = {
                        component.obtainEvent(ReviewStore.Intent.OnClickVk)
                    }
                )
                if (state.review.actualStarts.isEmpty() && state.isLoading) {
                    LoadingFullScreen(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                if (state.isError) {
                    FailedScreen(
                        message = state.message,
                    ) {
                        component.obtainEvent(ReviewStore.Intent.Launch(true))
                    }
                }
            }
        }
    }
}