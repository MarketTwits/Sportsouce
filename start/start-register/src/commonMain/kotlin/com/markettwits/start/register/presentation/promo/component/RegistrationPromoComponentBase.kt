package com.markettwits.start.register.presentation.promo.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.start.register.presentation.promo.store.RegistrationPromoStore
import com.markettwits.start.register.presentation.promo.store.RegistrationPromoStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistrationPromoComponentBase(
    componentContext: ComponentContext,
    private val promo: String,
    private val startId: Int,
    private val distancesId : List<Int>,
    private val storeFactory: RegistrationPromoStoreFactory,
    private val applyPromo: (String, Int) -> Unit,
    private val dismiss: () -> Unit,
) : RegistrationPromoComponent, ComponentContext by componentContext {

    private val scope = CoroutineScope(Dispatchers.Main)

    private val store = instanceKeeper.getStore {
        storeFactory.create(startId, distancesId,promo)
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<RegistrationPromoStore.State> = store.stateFlow

    override val labels: Flow<RegistrationPromoStore.Label> = store.labels

    override fun obtainEvent(intent: RegistrationPromoStore.Intent) {
        store.accept(intent)
    }

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is RegistrationPromoStore.Label.ApplyPromo -> applyPromo(it.promo, it.percent)
                    is RegistrationPromoStore.Label.Dismiss -> dismiss()
                    is RegistrationPromoStore.Label.Empty -> {}
                }
            }
        }
    }
}
