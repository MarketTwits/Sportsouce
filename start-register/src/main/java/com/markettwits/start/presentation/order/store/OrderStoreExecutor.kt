package com.markettwits.start.presentation.order.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.start.data.registration.RegistrationStartRepository
import com.markettwits.start.domain.StartStatement
import com.markettwits.start.presentation.order.domain.OrderStatement
import com.markettwits.start.presentation.order.store.OrderStore.Intent
import com.markettwits.start.presentation.order.store.OrderStore.Label
import com.markettwits.start.presentation.order.store.OrderStore.Message
import com.markettwits.start.presentation.order.store.OrderStore.State

class OrderStoreExecutor(private val repository: RegistrationStartRepository) :
    CoroutineExecutor<Intent, OrderStore.Action, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            Intent.ChangePaymentType -> TODO()
            Intent.GoBack -> publish(Label.GoBack)
            Intent.OnClickRegistry -> TODO()
            Intent.OnClickSave -> TODO()
            Intent.OnConsumedEvent -> TODO()
            is Intent.OnClickMember -> publish(Label.OnClickMember(intent.member, intent.id))
            is Intent.UpdateMember -> dispatch(
                Message.UpdateState(
                    applyMember(
                        currentState = getState().startStatement!!,
                        startStatement = intent.member,
                        id = intent.id
                    )
                )
            )

            is Intent.OnClickPromo -> publish(Label.OnClickPromo(intent.promo))
            is Intent.ApplyPromo -> dispatch(
                Message.UpdateState(
                    applyPromo(
                        getState().startStatement!!,
                        intent.promo,
                        intent.percent
                    )
                )
            )
        }
    }

    override fun executeAction(action: OrderStore.Action, getState: () -> State) {
        when (action) {
            is OrderStore.Action.InfoFailed -> dispatch(Message.PreloadFailed(action.message))
            is OrderStore.Action.InfoLoaded -> dispatch(Message.PreloadSuccess(action.orderStatement))
            is OrderStore.Action.Loading -> dispatch(Message.PreloadLoading)
        }
    }

    private fun applyMember(
        currentState: OrderStatement,
        startStatement: StartStatement,
        id: Int
    ): OrderStatement {
        val updatedMembers = currentState.members.toMutableList()
        if (id in updatedMembers.indices) {
            updatedMembers[id] = startStatement
        } else {
            throw IndexOutOfBoundsException("Error: Index out of bounds")
        }
        return currentState.copy(members = updatedMembers.toList())
    }

    private fun applyPromo(
        currentState: OrderStatement,
        promo: String,
        percent: Int
    ): OrderStatement {
        val discountPercent = percent.toDouble() / 100
        val currentPrice = currentState.orderPrice.total.toDouble()
        val discountAmount = currentPrice * discountPercent
        val discountedPrice = currentPrice - discountAmount
        val newOrderPrice = currentState.orderPrice.copy(
            discountInCache = discountAmount.toInt(),
            discountInPercent = percent,
            total = String.format("%.2f", discountedPrice)
        )
        return currentState.copy(orderPrice = newOrderPrice, promo = promo)
    }

}
