package com.markettwits.review.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.markettwits.core_ui.base_screen.LoadingFullScreen
import com.markettwits.core_ui.failed_screen.FailedScreen
import com.markettwits.core_ui.refresh.PullToRefreshScreen
import com.markettwits.review.presentation.components.content.ReviewContent
import com.markettwits.review.presentation.store.ReviewStore
import com.markettwits.start_search.search.presentation.components.publish.StartsSearchBarPublic
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun ReviewScreen(component: ReviewComponent) {
    val state by component.value.collectAsState()
    CollapsingToolbarScaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.primary),
        state = rememberCollapsingToolbarScaffoldState(),
        scrollStrategy = ScrollStrategy.EnterAlways,
        toolbar = {
            StartsSearchBarPublic(modifier = Modifier.clickable {
                component.obtainEvent(ReviewStore.Intent.OnClickSearch)
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
                val review = state.review
                ReviewContent(
                    news = review.news,
                    actual = review.actualStarts,
                    archive = review.archiveStarts,
                    onClickStart = {
                        component.obtainEvent(ReviewStore.Intent.OnClickItem(it))
                    },
                    onClickNewsInfo = {
                        component.obtainEvent(ReviewStore.Intent.OnClickNews(it))
                    },
                    onClickMenu = {
                        component.obtainEvent(ReviewStore.Intent.OnClickMenu(it))
                    })
                if (state.review.actualStarts.isEmpty() && state.isLoading) {
                    LoadingFullScreen(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                if (state.isError) {
                    FailedScreen(
                        message = state.message,
                        onClickHelp = {
                        },
                        onClickRetry = {
                            component.obtainEvent(ReviewStore.Intent.Launch(true))
                        }
                    )
                }
            }
        }
    }
}