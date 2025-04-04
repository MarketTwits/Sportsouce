package com.markettwits.sportsouce.news.news_list.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.sportsouce.news.common.model.NewsItem
import com.markettwits.sportsouce.news.news_list.store.NewsStore
import com.markettwits.sportsouce.news.news_list.store.NewsStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewsComponentBase(
    context: ComponentContext,
    private val storeFactory: NewsStoreFactory,
    private val onItemClick: (NewsItem) -> Unit
) : NewsComponent, ComponentContext by context {

    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }

    private val scope = CoroutineScope(Dispatchers.Main)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val value: StateFlow<NewsStore.State> = store.stateFlow

    override fun obtainEvent(event: NewsStore.Intent) {
        store.accept(event)
    }

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is NewsStore.Label.OnClickItem -> onItemClick(it.itemId)
                }
            }
        }
    }
}