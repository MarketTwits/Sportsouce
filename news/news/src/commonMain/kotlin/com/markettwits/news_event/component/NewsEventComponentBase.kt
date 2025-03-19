package com.markettwits.news_event.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.core.decompose.componentScope
import com.markettwits.news_event.store.NewsEventStore
import com.markettwits.news_event.store.NewsEventStoreFactory
import com.markettwits.news_list.domain.NewsInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewsEventComponentBase(
    context: ComponentContext,
    private val item: NewsInfo,
    private val storeFactory: NewsEventStoreFactory,
    private val onBack: () -> Unit,
) : NewsEventComponent, ComponentContext by context {

    private val store = instanceKeeper.getStore {
        storeFactory.create(item)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<NewsEventStore.State> = store.stateFlow

    override fun obtainEvent(event: NewsEventStore.Intent) {
        store.accept(event)
    }

    init {
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    is NewsEventStore.Label.Pop -> onBack()
                }
            }
        }
    }
}