package com.markettwits.sportsouce.news.news_list.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateLoading
import app.cash.paging.compose.LazyPagingItems
import com.markettwits.core.errors.api.composable.SauceErrorScreen
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.core.paging.fold
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.sportsouce.news.common.model.NewsHashtag
import com.markettwits.sportsouce.news.common.model.NewsItem
import com.markettwits.sportsouce.news.news_list.components.*
import com.markettwits.sportsouce.news.news_list.store.NewsStore

private enum class NewsUiState {
    Loading,
    Success,
    Empty,
    Error,
}

private data class NewsUiContent(
    val state: NewsUiState,
    val error: Throwable? = null,
)

@Composable
fun NewsScreenContent(
    state: NewsStore.State,
    newsItems: LazyPagingItems<NewsItem>,
    hashtagItems: LazyPagingItems<NewsHashtag>,
    onBack: () -> Unit,
    onIntent: (NewsStore.Intent) -> Unit,
) {
    val selectedCategoryName = state.categories
        .firstOrNull { it.id == state.selectedCategoryId }
        ?.name
        ?: "Новости"

    val selectedHashtags = state.selectedHashtag?.let(::listOf).orEmpty()

    val isRefreshing = newsItems.loadState.refresh is LoadStateLoading && newsItems.itemCount > 0

    val contentState = remember(newsItems.itemCount, newsItems.loadState.refresh, state.error) {
        var resolvedState = NewsUiState.Success
        var resolvedError: Throwable? = null

        newsItems.fold(
            onLoading = {
                resolvedState = NewsUiState.Loading
            },
            onException = { error ->
                resolvedState = NewsUiState.Error
                resolvedError = error
            },
            onSuccess = {
                resolvedState = NewsUiState.Success
            },
            onEmpty = {
                resolvedState = if (state.error != null) NewsUiState.Error else NewsUiState.Empty
            }
        )

        NewsUiContent(
            state = resolvedState,
            error = resolvedError ?: state.error
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        NewsToolbar(
            selectedHashtagsCount = if (state.selectedHashtag != null) 1 else 0,
            onBack = onBack,
            onOpenFilters = { onIntent(NewsStore.Intent.OnShowHashtagSheet) },
        )

        AdaptivePane(
            modifier = Modifier.weight(1f, fill = true),
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                if (state.categories.isNotEmpty()) {
                    NewsCategoriesRow(
                        categories = state.categories,
                        selectedCategoryId = state.selectedCategoryId,
                        onSelectCategory = {
                            onIntent(NewsStore.Intent.OnSelectCategory(it))
                        }
                    )
                }

                NewsSelectedHashtagsRow(
                    selectedHashtags = selectedHashtags,
                    onRemoveHashtag = {
                        onIntent(NewsStore.Intent.OnToggleHashtag(it))
                    }
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = true)
                ) {
                    PullToRefreshScreen(
                        modifier = Modifier.fillMaxSize(),
                        isRefreshing = isRefreshing,
                        onRefresh = {
                            onIntent(NewsStore.Intent.OnRefresh)
                            newsItems.refresh()
                            hashtagItems.refresh()
                        }
                    ) {
                        AnimatedContent(
                            targetState = contentState,
                            transitionSpec = {
                                fadeIn(animationSpec = tween(180))
                                    .togetherWith(fadeOut(animationSpec = tween(120)))
                            },
                            label = "NewsContentState"
                        ) { uiState ->
                            when (uiState.state) {
                                NewsUiState.Loading -> {
                                    NewsFeedLoadingList()
                                }

                                NewsUiState.Success -> {
                                    NewsFeedList(
                                        items = newsItems,
                                        badgeText = selectedCategoryName,
                                        onClickItem = {
                                            onIntent(NewsStore.Intent.OnClickItem(it))
                                        }
                                    )
                                }

                                NewsUiState.Error -> {
                                    (uiState.error ?: state.error)?.mapToSauceError()?.SauceErrorScreen(
                                        modifier = Modifier.fillMaxSize(),
                                        onClickRetry = {
                                            onIntent(NewsStore.Intent.OnRefresh)
                                            newsItems.refresh()
                                        },
                                    )
                                }

                                NewsUiState.Empty -> {
                                    NewsEmptyState(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (state.isHashtagSheetVisible) {
        NewsHashtagsBottomSheet(
            hashtags = hashtagItems,
            selectedHashtagId = state.selectedHashtag?.id,
            onDismiss = { onIntent(NewsStore.Intent.OnHideHashtagSheet) },
            onToggleHashtag = {
                onIntent(NewsStore.Intent.OnToggleHashtag(it))
            },
            onClearAll = {
                onIntent(NewsStore.Intent.OnClearHashtags)
            }
        )
    }
}
