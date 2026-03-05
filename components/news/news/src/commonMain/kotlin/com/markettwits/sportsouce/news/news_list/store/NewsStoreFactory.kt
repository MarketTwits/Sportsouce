package com.markettwits.sportsouce.news.news_list.store

import app.cash.paging.*
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.sportsouce.news.common.NewsRepository
import com.markettwits.sportsouce.news.common.model.NewsCategory
import com.markettwits.sportsouce.news.common.model.NewsHashtag
import com.markettwits.sportsouce.news.common.model.NewsItem
import com.markettwits.sportsouce.news.news_list.domain.NewsHashtagsPagingSource
import com.markettwits.sportsouce.news.news_list.domain.NewsPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

private const val NEWS_PAGE_SIZE = 10
private const val HASHTAGS_PAGE_SIZE = 20

class NewsStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: NewsRepository
) {

    fun create(): NewsStore =
        object : NewsStore,
            Store<NewsStore.Intent, NewsStore.State, NewsStore.Label> by storeFactory.create(
                name = "NewsStore",
                initialState = NewsStore.State(
                    newsItems = flowOf(PagingData.empty()),
                    hashtagItems = flowOf(PagingData.empty())
                ),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private sealed interface Msg {
        data class CategoriesLoaded(
            val categories: List<NewsCategory>,
        ) : Msg

        data class CategoriesFailed(val error: Throwable) : Msg
        data class NewsLoaded(val items: Flow<PagingData<NewsItem>>) : Msg
        data class HashtagsLoaded(val items: Flow<PagingData<NewsHashtag>>) : Msg
        data class SelectedCategoryChanged(val categoryId: Int?) : Msg
        data class SelectedHashtagChanged(val hashtag: NewsHashtag?) : Msg
        data class HashtagSheetVisibilityChanged(val isVisible: Boolean) : Msg
        data object ClearError : Msg
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<NewsStore.Intent, Unit, NewsStore.State, Msg, NewsStore.Label>() {

        override fun executeIntent(intent: NewsStore.Intent) {
            when (intent) {
                is NewsStore.Intent.Launch -> launchData()
                NewsStore.Intent.OnRefresh -> refreshData()
                is NewsStore.Intent.OnClickItem -> publish(NewsStore.Label.OnClickItem(intent.item))
                is NewsStore.Intent.OnSelectCategory -> {
                    if (state().selectedCategoryId == intent.categoryId) return
                    dispatch(Msg.SelectedCategoryChanged(intent.categoryId))
                    dispatch(Msg.ClearError)
                    reloadNews()
                }

                is NewsStore.Intent.OnToggleHashtag -> {
                    val selected = if (state().selectedHashtag?.id == intent.hashtag.id) null else intent.hashtag
                    dispatch(Msg.SelectedHashtagChanged(selected))
                    dispatch(Msg.HashtagSheetVisibilityChanged(false))
                    dispatch(Msg.ClearError)
                    reloadNews()
                }

                NewsStore.Intent.OnShowHashtagSheet -> {
                    dispatch(Msg.HashtagSheetVisibilityChanged(true))
                }

                NewsStore.Intent.OnHideHashtagSheet -> {
                    dispatch(Msg.HashtagSheetVisibilityChanged(false))
                }

                NewsStore.Intent.OnClearHashtags -> {
                    dispatch(Msg.SelectedHashtagChanged(null))
                    dispatch(Msg.ClearError)
                    reloadNews()
                }
            }
        }

        override fun executeAction(action: Unit) {
            launchData()
        }

        private fun launchData() {
            scope.launch {
                dispatch(Msg.ClearError)
                repository.categories()
                    .onFailure {
                        dispatch(
                            Msg.CategoriesFailed(
                                it
                            )
                        )
                    }
                    .onSuccess { categories ->
                        dispatch(
                            Msg.CategoriesLoaded(
                                categories = categories,
                            )
                        )
                    }

                dispatch(Msg.HashtagsLoaded(createHashtagsFlow()))
                reloadNews()
            }
        }

        private fun refreshData() {
            scope.launch {
                dispatch(Msg.ClearError)
                repository.categories()
                    .onFailure {
                        dispatch(Msg.CategoriesFailed(it))
                    }
                    .onSuccess { categories ->
                        dispatch(Msg.CategoriesLoaded(categories = categories))
                    }
                dispatch(Msg.HashtagsLoaded(createHashtagsFlow()))
                reloadNews()
            }
        }

        private fun reloadNews() {
            scope.launch {
                dispatch(
                    Msg.NewsLoaded(
                        items = createNewsFlow(
                            categoryId = state().selectedCategoryId,
                            hashtag = state().selectedHashtag?.name
                        )
                    )
                )
            }
        }

        private fun createNewsFlow(
            categoryId: Int?,
            hashtag: String?,
        ): Flow<PagingData<NewsItem>> {
            val pagingConfig = PagingConfig(
                pageSize = NEWS_PAGE_SIZE,
                initialLoadSize = NEWS_PAGE_SIZE,
                prefetchDistance = NEWS_PAGE_SIZE / 2,
                enablePlaceholders = false,
                maxSize = Int.MAX_VALUE,
                jumpThreshold = Int.MIN_VALUE
            )
            val pager: Pager<Int, NewsItem> = Pager(
                pagingConfig,
                null
            ) {
                NewsPagingSource(
                    repository = repository,
                    categoryId = categoryId,
                    hashtag = hashtag,
                    pageSize = NEWS_PAGE_SIZE
                ) as PagingSource<Int, NewsItem>
            }
            return pager.flow.cachedIn(scope)
        }

        private fun createHashtagsFlow(): Flow<PagingData<NewsHashtag>> {
            val pagingConfig = PagingConfig(
                pageSize = HASHTAGS_PAGE_SIZE,
                initialLoadSize = HASHTAGS_PAGE_SIZE,
                prefetchDistance = HASHTAGS_PAGE_SIZE / 2,
                enablePlaceholders = false,
                maxSize = Int.MAX_VALUE,
                jumpThreshold = Int.MIN_VALUE
            )
            val pager: Pager<Int, NewsHashtag> = Pager(
                pagingConfig,
                null
            ) {
                NewsHashtagsPagingSource(
                    repository = repository,
                    pageSize = HASHTAGS_PAGE_SIZE
                ) as PagingSource<Int, NewsHashtag>
            }
            return pager.flow.cachedIn(scope)
        }
    }

    private object ReducerImpl : Reducer<NewsStore.State, Msg> {
        override fun NewsStore.State.reduce(msg: Msg): NewsStore.State =
            when (msg) {
                is Msg.CategoriesLoaded -> copy(
                    categories = msg.categories,
                )

                is Msg.CategoriesFailed -> copy(
                    error = msg.error
                )

                is Msg.NewsLoaded -> copy(
                    newsItems = msg.items
                )

                is Msg.HashtagsLoaded -> copy(
                    hashtagItems = msg.items
                )

                is Msg.SelectedCategoryChanged -> copy(
                    selectedCategoryId = msg.categoryId
                )

                is Msg.SelectedHashtagChanged -> copy(
                    selectedHashtag = msg.hashtag
                )

                is Msg.HashtagSheetVisibilityChanged -> copy(
                    isHashtagSheetVisible = msg.isVisible
                )

                Msg.ClearError -> copy(
                    error = null
                )
            }
    }
}
