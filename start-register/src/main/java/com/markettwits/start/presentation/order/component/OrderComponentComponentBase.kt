package com.markettwits.start.presentation.order.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.cloud.ext_model.DistanceItem
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
    private val paymentType: String,
    private val distanceInfo: DistanceItem,
    private val paymentDisabled: Boolean,
    private val storeFactory: OrderStoreFactory,
    private val pop: () -> Unit,
    private val onClickMember: (StartStatement, Int) -> Unit,
    private val onClickPromo: (Int, String) -> Unit
) : OrderComponentComponent,
    ComponentContext by componentContext {
    private val scope = CoroutineScope(Dispatchers.Main)

    private val restoreState: MutableValue<OrderStore.State> = MutableValue(
        stateKeeper.consume(
            key = ORDER_STORE_STATE_KEY,
            OrderStore.State.serializer()
        ) ?: OrderStore.State()
    )

    private val store = instanceKeeper.getStore {
        storeFactory.create(
            state = restoreState.value,
            distanceInfo = distanceInfo,
            starId = startId,
            paymentType = paymentType,
            paymentDisabled = paymentDisabled
        )
    }
    override val labels: Flow<OrderStore.Label> = store.labels
    override val model: StateFlow<OrderStore.State> = store.stateFlow
    override fun obtainEvent(event: OrderStore.Intent) {
        store.accept(event)
    }

    init {
        stateKeeper.register(
            key = ORDER_STORE_STATE_KEY,
            strategy = OrderStore.State.serializer()
        ) {
            store.stateFlow.value
        }
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

    private companion object {
        const val ORDER_STORE_STATE_KEY = "ORDER_STORE_STATE_KEY"
    }
}
