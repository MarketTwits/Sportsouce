package com.markettwits.start.register.presentation.order.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.members.member_common.domain.ProfileMember
import com.markettwits.start.register.domain.StartStatement
import com.markettwits.start.register.presentation.order.presentation.store.OrderStore
import com.markettwits.start.register.presentation.order.presentation.store.OrderStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderComponentComponentBase(
    componentContext: ComponentContext,
    private val startId: Int,
    private val startTitle: String,
    private val paymentType: String,
    private val distanceInfo: DistanceItem,
    private val discounts: List<DistanceItem.Discount>,
    private val paymentDisabled: Boolean,
    private val storeFactory: OrderStoreFactory,
    private val pop: () -> Unit,
    private val onClickMember: (StartStatement, Int, List<ProfileMember>) -> Unit,
    private val onClickPromo: (Int, String) -> Unit,
    private val openSuccess: () -> Unit
) : OrderComponentComponent,
    ComponentContext by componentContext {
    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    private val store = instanceKeeper.getStore {
        storeFactory.create(
            state = stateKeeper.consume(ORDER_STORE_STATE_KEY, OrderStore.State.serializer()),
            distanceInfo = distanceInfo,
            starId = startId,
            paymentType = paymentType,
            paymentDisabled = paymentDisabled,
            startTitle = startTitle,
            discounts = discounts
        )
    }

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
                    is OrderStore.Label.OnClickMember -> onClickMember(
                        it.member,
                        it.memberId,
                        it.membersProfile
                    )
                    is OrderStore.Label.GoBack -> pop()
                    is OrderStore.Label.OnClickPromo -> onClickPromo(startId, it.promo)
                    is OrderStore.Label.GoSuccess -> openSuccess()
                }
            }
        }
    }

    private companion object {
        const val ORDER_STORE_STATE_KEY = "ORDER_STORE_STATE_KEY"
    }
}
