package com.markettwits.start.presentation.comments.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.start.presentation.comments.store.StartCommentsStore
import com.markettwits.start.presentation.comments.store.StartCommentsStoreFactory
import kotlinx.coroutines.flow.StateFlow

class StartCommentsComponentBase(
    context: ComponentContext,
    private val storeFactory: StartCommentsStoreFactory,
    private val startId: Int,
) : ComponentContext by context, StartCommentsComponent {
    private val store = instanceKeeper.getStore {
        storeFactory.create(startId)
    }
    override val state: StateFlow<StartCommentsStore.State> = store.stateFlow
    override fun obtainEvent(intent: StartCommentsStore.Intent) {
        store.accept(intent)
    }
}