package com.markettwits.news_event

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.news_event.store.NewsEventStore
import com.markettwits.news_event.store.NewsEventStoreFactory
import com.markettwits.news_list.domain.NewsInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewsEventComponentBase(
    context: ComponentContext,
    private val item: NewsInfo,
    private val storeFactory: NewsEventStoreFactory,
    private val pop: () -> Unit,
) : NewsEventComponent,
    ComponentContext by context {
    private val store = instanceKeeper.getStore {
        storeFactory.create(item)
    }
    override val state: StateFlow<NewsEventStore.State> = store.stateFlow
    private val scope = CoroutineScope(Dispatchers.Main)
    override fun obtainEvent(event: NewsEventStore.Intent) {
        store.accept(event)
    }

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is NewsEventStore.Label.Pop -> pop()
                }
            }
        }
    }
}