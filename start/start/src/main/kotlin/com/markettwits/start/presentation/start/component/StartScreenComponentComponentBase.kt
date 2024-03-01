package com.markettwits.start.presentation.start.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.start.presentation.membres.list.StartMembersUi
import com.markettwits.start.presentation.start.store.StartScreenStore
import com.markettwits.start.presentation.start.store.StartScreenStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StartScreenComponentComponentBase(
    componentContext: ComponentContext,
    private val startId: Int,
    private val back: () -> Unit,
    private val register: (DistanceItem, Boolean, String) -> Unit,
    private val storeFactory: StartScreenStoreFactory,
    private val members: (Int, List<StartMembersUi>) -> Unit,
) : ComponentContext by componentContext, StartScreenComponent {
    private val store = instanceKeeper.getStore {
        storeFactory.create(startId)
    }
    private val scope = CoroutineScope(Dispatchers.Main)
    override val start: StateFlow<StartScreenStore.State> = store.stateFlow

    override fun obtainEvent(intent: StartScreenStore.Intent) {
        store.accept(intent)
    }

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is StartScreenStore.Label.OnClickBack -> back()
                    is StartScreenStore.Label.OnClickDistance -> register(
                        it.distanceInfo,
                        it.paymentDisabled,
                        it.paymentType
                    )

                    is StartScreenStore.Label.OnClickMembers -> members(startId, it.members)
                }
            }
        }
    }
}