package com.markettwits.start.presentation.order.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.start.data.registration.RegistrationStartRepository
import com.markettwits.start.domain.StartStatement
import com.markettwits.start.presentation.order.store.OrderStore
import com.markettwits.start.presentation.order.store.OrderStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderComponentComponentBase(
    componentContext: ComponentContext,
    private val startId: Int,
    private val distanceInfo: DistanceItem,
    private val paymentDisabled: Boolean,
    private val repository: RegistrationStartRepository,
    private val pop: () -> Unit,
    private val onClickMember: (StartStatement, Int) -> Unit,
    private val onClickPromo: (Int, String) -> Unit
) : OrderComponentComponent,
    ComponentContext by componentContext {
    private val scope = CoroutineScope(Dispatchers.Main)
    private val store = instanceKeeper.getStore {
        OrderStoreFactory(DefaultStoreFactory(), repository).create(
            distanceInfo = distanceInfo,
            starId = startId,
            paymentDisabled = paymentDisabled
        )
    }
    override val labels: Flow<OrderStore.Label> = store.labels
    override val model: StateFlow<OrderStore.State> = store.stateFlow
    override fun obtainEvent(event: OrderStore.Intent) {
        store.accept(event)
    }

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is OrderStore.Label.OnClickMember -> onClickMember(it.member, it.memberId)
                    is OrderStore.Label.GoBack -> pop()
                    is OrderStore.Label.OnClickPromo -> onClickPromo(startId, it.promo)
                }
            }
        }
    }
}
