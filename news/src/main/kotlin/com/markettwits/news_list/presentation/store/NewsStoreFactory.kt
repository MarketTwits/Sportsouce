package com.markettwits.news_list.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.news_event.store.NewsEventStore
import com.markettwits.news_event.store.NewsEventStoreFactory
import com.markettwits.news_list.data.NewsDataSource
import com.markettwits.news_list.domain.NewsInfo
import kotlinx.coroutines.launch

class NewsStoreFactory(
    private val storeFactory: StoreFactory,
    private val dataSource: NewsDataSource
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
        data class InfoLoaded(val data: List<NewsInfo>) : Msg
        data class InfoFailed(val message: String) : Msg
    }


    private inner class ExecutorImpl :
        CoroutineExecutor<NewsStore.Intent, Unit, NewsStore.State, Msg, NewsStore.Label>() {
        override fun executeIntent(intent: NewsStore.Intent, getState: () -> NewsStore.State) {
            when (intent) {
                is NewsStore.Intent.Launch -> {
                    launch()
                }
                is NewsStore.Intent.OnClickItem -> publish(NewsStore.Label.OnClickItem(intent.item))
            }
        }

        override fun executeAction(action: Unit, getState: () -> NewsStore.State) {
            launch()
        }

        fun launch() {
            scope.launch {
                dispatch(Msg.Loading)
                dataSource.news()
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
        override fun NewsStore.State.reduce(message: Msg): NewsStore.State =
            when (message) {
                is Msg.InfoFailed -> NewsStore.State(message = message.message, isError = true)
                is Msg.InfoLoaded -> NewsStore.State(news = message.data)
                is Msg.Loading -> NewsStore.State(isLoading = true)
            }
    }
}