package com.markettwits.news.news_event.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.news.common.model.NewsItem

class NewsEventStoreFactory(
    private val storeFactory: StoreFactory,
) {

    fun create(item : NewsItem): NewsEventStore =
        object : NewsEventStore,
            Store<NewsEventStore.Intent, NewsEventStore.State, NewsEventStore.Label> by storeFactory.create(
                name = "NewsStore",
                initialState = NewsEventStore.State(news = item),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private sealed interface Msg {
        data class Show(val data: NewsItem) : Msg
    }


    private inner class ExecutorImpl :
        CoroutineExecutor<NewsEventStore.Intent, Unit, NewsEventStore.State, Msg, NewsEventStore.Label>() {
        override fun executeIntent(intent: NewsEventStore.Intent) {
            when (intent) {
                NewsEventStore.Intent.Pop -> publish(NewsEventStore.Label.Pop)
            }
        }

        override fun executeAction(action: Unit) = Unit
    }

    private object ReducerImpl : Reducer<NewsEventStore.State, Msg> {
        override fun NewsEventStore.State.reduce(msg: Msg): NewsEventStore.State =
            when (msg) {
                is Msg.Show -> NewsEventStore.State(news = msg.data)
            }
    }
}
