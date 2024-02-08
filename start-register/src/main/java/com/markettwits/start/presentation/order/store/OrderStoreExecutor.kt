package com.markettwits.start.presentation.order.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.core_ui.event.consumed
import com.markettwits.start.presentation.order.store.OrderStore.Intent
import com.markettwits.start.presentation.order.store.OrderStore.Label
import com.markettwits.start.presentation.order.store.OrderStore.Message
import com.markettwits.start.presentation.order.store.OrderStore.State

class OrderStoreExecutor(
    private val handle: OrderStoreExecutorHandle,
    private val distanceItem: DistanceItem,
    private val startId: Int
) :
    CoroutineExecutor<Intent, OrderStore.Action, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.ChangePaymentType -> dispatch(
                Message.UpdateState(
                    handle.changePaymentType(
                        getState(),
                        intent.payNow
                    )
                )
            )

            is Intent.GoBack -> publish(Label.GoBack)
            is Intent.OnClickRegistry -> handle.onClickRegistry(getState(), distanceItem, startId) {
                dispatch(Message.UpdateState(it))
            }
            is Intent.OnClickMember -> publish(Label.OnClickMember(intent.member, intent.id))
            is Intent.UpdateMember -> dispatch(
                Message.UpdateState(
                    getState().copy(
                        orderStatement = handle.applyMember(
                            currentState = getState().orderStatement!!,
                            startStatement = intent.member,
                            id = intent.id
                        )
                    )
                )
            )

            is Intent.OnClickPromo -> publish(Label.OnClickPromo(intent.promo))
            is Intent.ApplyPromo -> dispatch(
                Message.UpdateState(
                    getState().copy(
                        orderStatement = handle.applyPromo(
                            getState().orderStatement!!,
                            intent.promo,
                            intent.percent
                        )
                    )
                )
            )

            is Intent.OnClickCheckedRules -> dispatch(
                Message.UpdateState(
                    getState().copy(
                        orderStatement = handle.onClickCheckRules(getState().orderStatement!!)
                    )
                )
            )

            is Intent.OnConsumedEvent -> dispatch(Message.UpdateState(getState().copy(event = consumed())))
        }
    }

    override fun executeAction(action: OrderStore.Action, getState: () -> State) {
        when (action) {
            is OrderStore.Action.InfoFailed -> dispatch(Message.PreloadFailed(action.message))
            is OrderStore.Action.InfoLoaded -> dispatch(Message.PreloadSuccess(action.orderStatement))
            is OrderStore.Action.Loading -> dispatch(Message.PreloadLoading)
        }
    }
}
