package com.markettwits.sportsouce.news.news_event.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.sportsouce.news.common.NewsRepository
import com.markettwits.sportsouce.news.common.model.NewsItem
import com.markettwits.sportsouce.news.news_event.component.NewsEventInput
import kotlinx.coroutines.launch

class NewsEventStoreFactory(
    private val storeFactory: StoreFactory,
    private val newsRepository: NewsRepository,
) {

    fun create(input: NewsEventInput): NewsEventStore =
        object : NewsEventStore,
            Store<NewsEventStore.Intent, NewsEventStore.State, NewsEventStore.Label> by storeFactory.create(
                name = "NewsEventStore",
                initialState = getInitialState(input),
                bootstrapper = SimpleBootstrapper(input),
                executorFactory = { ExecutorImpl(input) },
                reducer = ReducerImpl
            ) {}

    private fun getInitialState(input: NewsEventInput): NewsEventStore.State {
        return when (input) {
            is NewsEventInput.Item -> NewsEventStore.State(
                isLoading = false,
                news = input.item,
                error = null
            )

            is NewsEventInput.Id -> NewsEventStore.State(
                isLoading = true,
                news = null,
                error = null
            )
        }
    }

    private sealed interface Msg {
        data object Loading : Msg
        data class Success(val news: NewsItem) : Msg
        data class Error(val error: Throwable) : Msg
    }

    private inner class ExecutorImpl(
        private val input: NewsEventInput
    ) : CoroutineExecutor<NewsEventStore.Intent, NewsEventInput, NewsEventStore.State, Msg, NewsEventStore.Label>() {
        
        override fun executeIntent(intent: NewsEventStore.Intent) {
            when (intent) {
                NewsEventStore.Intent.Pop -> publish(NewsEventStore.Label.Pop)
                NewsEventStore.Intent.Retry -> loadNews()
            }
        }

        override fun executeAction(action: NewsEventInput) {
            when (action) {
                is NewsEventInput.Item -> {
                    // News item already provided, no need to load
                }

                is NewsEventInput.Id -> {
                    // Need to load news by ID
                    loadNews()
                }
            }
        }

        private fun loadNews() {
            when (input) {
                is NewsEventInput.Id -> {
                    scope.launch {
                        dispatch(Msg.Loading)
                        newsRepository.newsItem(input.id).fold(
                            onSuccess = { newsItem ->
                                dispatch(Msg.Success(newsItem))
                            },
                            onFailure = { error ->
                                dispatch(Msg.Error(error))
                            }
                        )
                    }
                }

                is NewsEventInput.Item -> {
                    // Already have the item, dispatch success
                    dispatch(Msg.Success(input.item))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<NewsEventStore.State, Msg> {
        override fun NewsEventStore.State.reduce(msg: Msg): NewsEventStore.State =
            when (msg) {
                is Msg.Loading -> copy(
                    isLoading = true,
                    error = null
                )

                is Msg.Success -> copy(
                    isLoading = false,
                    news = msg.news,
                    error = null
                )

                is Msg.Error -> copy(
                    isLoading = false,
                    error = msg.error
                )
            }
    }
}
