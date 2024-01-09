package com.markettwits.review.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.news_list.presentation.store.NewsStore
import com.markettwits.review.presentation.store.ReviewStore
import com.markettwits.review.presentation.store.ReviewStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReviewComponentBase(
    context: ComponentContext,
    private val storeFactory: ReviewStoreFactory,
    private val onStartClick: (Int) -> Unit
) : ReviewComponent, ComponentContext by context {

    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }
    private val scope = CoroutineScope(Dispatchers.Main)
    override val value: StateFlow<ReviewStore.State> = store.stateFlow
    override fun obtainEvent(event: ReviewStore.Intent) {
        store.accept(event)
    }

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is ReviewStore.Label.OnClickItem -> onStartClick(it.item)
                }
            }
        }
    }
}