package com.markettwits.start.presentation.order.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.core_ui.event.EventContent
import com.markettwits.core_ui.event.consumed
import com.markettwits.core_ui.event.triggered
import com.markettwits.start.domain.StartStatement
import com.markettwits.start.presentation.order.domain.OrderStatement
import com.markettwits.start.presentation.order.domain.interactor.OrderInteractor
import com.markettwits.start.presentation.order.store.OrderStore.Intent
import com.markettwits.start.presentation.order.store.OrderStore.Label
import com.markettwits.start.presentation.order.store.OrderStore.Message
import com.markettwits.start.presentation.order.store.OrderStore.State
import kotlinx.coroutines.launch

class OrderStoreExecutor(
    private val interactor: OrderInteractor,
    private val distanceItem: DistanceItem,
    private val startId: Int
) :
    CoroutineExecutor<Intent, OrderStore.Action, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.ChangePaymentType -> changePaymentType(getState(), intent.payNow)
            Intent.GoBack -> publish(Label.GoBack)
            Intent.OnClickRegistry -> onClickRegistry(getState(), distanceItem, startId)
            is Intent.OnClickMember -> publish(Label.OnClickMember(intent.member, intent.id))
            is Intent.UpdateMember -> dispatch(
                Message.UpdateState(
                    getState().copy(
                        orderStatement =
                        applyMember(
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
                        orderStatement =
                        applyPromo(
                            getState().orderStatement!!,
                            intent.promo,
                            intent.percent
                        )
                    )
                )
            )

            Intent.OnClickCheckedRules -> dispatch(
                Message.UpdateState(
                    getState().copy(
                        orderStatement
                        = onClickCheckRules(getState().orderStatement!!)
                    )
                )
            )

            Intent.OnConsumedEvent -> dispatch(Message.UpdateState(getState().copy(event = consumed())))
        }
    }

    private fun changePaymentType(state: State, payNow: Boolean) {
        val update = state.orderStatement?.copy(payNow = payNow)
        val newState = state.copy(orderStatement = update)
        dispatch(Message.UpdateState(newState))
    }

    private fun onClickRegistry(state: State, distanceItem: DistanceItem, startId: Int) {
        scope.launch {
            interactor.valid(checkNotNull(state.orderStatement)).fold(onSuccess = {
                val result = interactor.registry(
                    checkNotNull(state.orderStatement),
                    distanceItem,
                    startId
                )
                if (result.isError) {
                    showMessage(state, false, result.message)
                }
                if (result.isSuccess) {
                    if (result.paymentUrl.isNotEmpty()) {
                        showMessage(state, true, result.paymentUrl)
                    } else {
                        showMessage(state, true, "Вы успешно зарегестрировались на старт")
                    }
                }
            }, onFailure = {
                showMessage(state, false, it.message.toString())
            })
        }
    }

    private fun showMessage(state: State, success: Boolean, message: String) {
        dispatch(
            Message.UpdateState(
                state.copy(
                    event = triggered(
                        EventContent(
                            success = success,
                            message
                        )
                    )
                )
            )
        )
    }

    override fun executeAction(action: OrderStore.Action, getState: () -> State) {
        when (action) {
            is OrderStore.Action.InfoFailed -> dispatch(Message.PreloadFailed(action.message))
            is OrderStore.Action.InfoLoaded -> dispatch(Message.PreloadSuccess(action.orderStatement))
            is OrderStore.Action.Loading -> dispatch(Message.PreloadLoading)
        }
    }

    private fun onClickCheckRules(state: OrderStatement): OrderStatement {
        val newValue = !state.checkPolitics
        return state.copy(checkPolitics = newValue)
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
        val currentPrice = currentState.orderPrice.total
        val discountAmount = currentPrice * discountPercent
        val discountedPrice = currentPrice - discountAmount
        val newOrderPrice = currentState.orderPrice.copy(
            discountInCache = discountAmount,
            discountInPercent = percent,
            total = discountedPrice
        )
        return currentState.copy(orderPrice = newOrderPrice, promo = promo)
    }

}
