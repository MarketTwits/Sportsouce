package com.markettwits.sportsouce.news.news_list.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.sportsouce.news.common.NewsRepository
import com.markettwits.sportsouce.news.common.model.NewsItem
import kotlinx.coroutines.launch

class NewsStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: NewsRepository
) {

    fun create(): NewsStore =
        object : NewsStore,
            Store<NewsStore.Intent, NewsStore.State, NewsStore.Label> by storeFactory.create(
                name = "NewsStore",
                initialState = NewsStore.State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private sealed interface Msg {
        data object Loading : Msg
        data class InfoLoaded(val data: List<NewsItem>) : Msg
        data class InfoFailed(val message: String) : Msg
    }


    private inner class ExecutorImpl :
        CoroutineExecutor<NewsStore.Intent, Unit, NewsStore.State, Msg, NewsStore.Label>() {
        override fun executeIntent(intent: NewsStore.Intent) {
            when (intent) {
                is NewsStore.Intent.Launch -> {
                    launch()
                }
                is NewsStore.Intent.OnClickItem -> publish(NewsStore.Label.OnClickItem(intent.item))
            }
        }

        override fun executeAction(action: Unit) {
            launch()
        }

        fun launch() {
            scope.launch {
                dispatch(Msg.Loading)
                repository.news()
                    .onFailure {
                        dispatch(Msg.InfoFailed(it.message.toString()))
                    }
                    .onSuccess {
                        dispatch(Msg.InfoLoaded(it))
                    }
            }
        }
    }

    private object ReducerImpl : Reducer<NewsStore.State, Msg> {
        override fun NewsStore.State.reduce(msg: Msg): NewsStore.State =
            when (msg) {
                is Msg.InfoFailed -> NewsStore.State(message = msg.message, isError = true)
                is Msg.InfoLoaded -> NewsStore.State(news = msg.data)
                is Msg.Loading -> NewsStore.State(isLoading = true)
            }
    }
}