package com.markettwits.sportsouce.review.review.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.news.common.model.NewsItem
import com.markettwits.sportsouce.review.review.presentation.store.ReviewStore
import com.markettwits.sportsouce.review.review.presentation.store.ReviewStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ReviewComponentBase(
    context: ComponentContext,
    private val storeFactory: ReviewStoreFactory,
    private val onClickMenu: (Int) -> Unit,
    private val onStartClick: (Int) -> Unit,
    private val onClickNews: (NewsItem) -> Unit,
    private val onClickSearch: () -> Unit,
    private val onClickSettings: () -> Unit,
) : ReviewComponent, ComponentContext by context {

    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }
    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    @OptIn(ExperimentalCoroutinesApi::class)
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