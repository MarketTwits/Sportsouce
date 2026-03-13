package com.markettwits.sportsouce.news.news_list.store

import app.cash.paging.PagingData
import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.sportsouce.news.common.model.NewsCategory
import com.markettwits.sportsouce.news.common.model.NewsHashtag
import com.markettwits.sportsouce.news.common.model.NewsItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface NewsStore : Store<NewsStore.Intent, NewsStore.State, NewsStore.Label> {

    sealed interface Intent {
        data object Launch : Intent
        data object OnRefresh : Intent
        data class OnClickItem(val item: NewsItem) : Intent
        data class OnSelectCategory(val categoryId: Int?) : Intent
        data class OnToggleHashtag(val hashtag: NewsHashtag) : Intent
        data object OnShowHashtagSheet : Intent
        data object OnHideHashtagSheet : Intent
        data object OnClearHashtags : Intent
    }

    data class State(
        val newsItems: Flow<PagingData<NewsItem>> = flowOf(PagingData.empty()),
        val hashtagItems: Flow<PagingData<NewsHashtag>> = flowOf(PagingData.empty()),
        val categories: List<NewsCategory> = emptyList(),
        val selectedCategoryId: Int? = null,
        val selectedHashtag: NewsHashtag? = null,
        val isHashtagSheetVisible: Boolean = false,
        val error: Throwable? = null,
    )

    sealed interface Label {
        data class OnClickItem(val item: NewsItem) : Label
    }
}
