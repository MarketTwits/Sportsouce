package com.markettwits.random.random.presentation

import android.app.ProgressDialog.show
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.random.random.presentation.store.StartRandomStore
import com.markettwits.random.random.presentation.store.StartRandomStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class StartRandomComponentBase(
    context: ComponentContext,
    private val storeFactory: StartRandomStoreFactory,
    private val openStart : (Int) -> Unit
) : StartRandomComponent, ComponentContext by context {
    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }
    private val scope = CoroutineScope(Dispatchers.Main)
    override val value: StateFlow<StartRandomStore.State> = store.stateFlow
    override fun obtainEvent(event: StartRandomStore.Intent) {
        store.accept(event)
    }

    init {
        scope.launch {
            store.labels.collect {
                when(it){
                    is StartRandomStore.Label.OpenStart -> openStart(it.startId)
                }
            }
        }
    }
}