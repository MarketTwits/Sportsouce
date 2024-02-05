package com.markettwits.start.presentation.registration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.start.presentation.registration.store.StartRegistrationStore
import com.markettwits.start.presentation.registration.store.StartRegistrationStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StartRegistrationComponentBase(
    context: ComponentContext,
    private val startId: Int,
    private val distanceInfo: DistanceItem,
    private val paymentDisabled : Boolean,
    private val storeFactory: StartRegistrationStoreFactory,
    private val pop : () -> Unit,
) : StartRegistrationComponent, ComponentContext by context {
    val scope = CoroutineScope(Dispatchers.Main)
    private val store = instanceKeeper.getStore {
        storeFactory.create(
            distanceInfo = distanceInfo,
            starId = startId,
            paymentDisabled = paymentDisabled
        )
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
