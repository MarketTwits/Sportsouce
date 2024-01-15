package com.markettwits.start.presentation.registration

import android.app.ProgressDialog.show
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.start.presentation.registration.store.StartRegistrationStore
import com.markettwits.start.presentation.registration.store.StartRegistrationStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StartRegistrationComponentBase(
    context: ComponentContext,
    private val startId: Int,
    private val storeFactory: StartRegistrationStoreFactory,
    private val pop : () -> Unit,
) : StartRegistrationComponent, ComponentContext by context {
    val scope = CoroutineScope(Dispatchers.Main)
    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }

    override fun obtainEvent(event: StartRegistrationStore.Intent) {
        store.accept(event)
    }

    override val value: StateFlow<StartRegistrationStore.State> = store.stateFlow


    init {
        scope.launch {
            store.labels.collect {
               when(it){
                   is StartRegistrationStore.Label.GoBack -> pop()
               }
            }
        }
    }
}
