package com.markettwits.review.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.news_list.domain.NewsInfo
import com.markettwits.review.presentation.store.ReviewStore
import com.markettwits.review.presentation.store.ReviewStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ReviewComponentBase(
    context: ComponentContext,
    private val storeFactory: ReviewStoreFactory,
    private val onClickMenu: (Int) -> Unit,
    private val onStartClick: (Int) -> Unit,
    private val onClickNews: (NewsInfo) -> Unit,
    private val onClickSearch: () -> Unit,
    private val onClickSettings: () -> Unit,
) : ReviewComponent, ComponentContext by context {

    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }
    private val scope = CoroutineScope(Dispatchers.Main.immediate)
    override val value: StateFlow<ReviewStore.State> = store.stateFlow
    override fun obtainEvent(event: ReviewStore.Intent) {
        store.accept(event)
    }

    init {
        store.labels.onEach {
            when (it) {
                is ReviewStore.Label.OnClickItem -> onStartClick(it.item)
                is ReviewStore.Label.OnClickMenu -> onClickMenu(it.item)
                is ReviewStore.Label.OnClickSearch -> onClickSearch()
                is ReviewStore.Label.OnClickNews -> onClickNews(it.news)
                is ReviewStore.Label.OnClickSettings -> onClickSettings()
            }
        }.launchIn(scope)
    }
}