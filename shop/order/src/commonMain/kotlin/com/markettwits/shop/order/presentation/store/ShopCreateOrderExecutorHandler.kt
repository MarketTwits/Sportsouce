package com.markettwits.shop.order.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.IntentAction
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.core.errors.api.throwable.mapToString
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.event.consumed
import com.markettwits.core_ui.items.event.triggered
import com.markettwits.shop.cart.domain.ShopCartRepository
import com.markettwits.shop.cart.domain.ShopItemCart
import com.markettwits.shop.cart.domain.calculateTotalCost
import com.markettwits.shop.order.domain.ShopOrderRepository
import com.markettwits.shop.order.domain.model.ShopDeliveryType
import com.markettwits.shop.order.domain.model.ShopOrderPrice
import com.markettwits.shop.order.domain.model.ShopPaymentType
import com.markettwits.shop.order.domain.model.ShopRecipient
import com.markettwits.shop.order.domain.model.containsUnAvailableItems
import com.markettwits.shop.order.domain.model.getAvailableItems
import com.markettwits.shop.order.domain.model.getUrlAfterCreateOrder
import com.markettwits.shop.order.domain.model.isAvailableToCreateOrder
import com.markettwits.shop.order.domain.model.isValid
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore.Intent
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore.Label
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore.Message
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore.State
import kotlinx.coroutines.launch

abstract class ShopCreateOrderExecutorHandler(
    private val orderRepository: ShopOrderRepository,
    private val cartRepository: ShopCartRepository,
    private val intentAction: IntentAction
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {

    protected fun onClickChangePaymentType(state: State, paymentType: ShopPaymentType) {
        dispatch(
            Message.UpdateOrderState(
                order = state.copy(paymentType = state.paymentType.copy(selectedOption = paymentType))
            )
        )
    }

    protected fun onClickChangeDeliveryType(state: State, deliveryType: ShopDeliveryType) {
        dispatch(
            Message.UpdateOrderState(
                order = state.copy(deliveryType = state.deliveryType.copy(selectedOption = deliveryType))
            )
        )
    }

    protected fun onClickUpdateRecipient(state: State.ShopRecipientState) {
        val recipient = state.mutableShopRecipient
        recipient.isValid().fold(
            onSuccess = {
                dispatch(
                    Message.UpdateShopRecipientState(
                        state.copy(
                            isExpanded = false,
                            isAvailable = true,
                            currentShopRecipient = recipient,
                            mutableShopRecipient = recipient,
                            message = ""
                        )
                    )
                )
            }, onFailure = {
                dispatch(
                    Message.UpdateShopRecipientState(
                        state.copy(
                            isAvailable = false,
                            message = it.message.toString()
                        )
                    )
                )
            }
        )
    }

    protected fun onClickChangeRecipientBottomSheetState(state: State.ShopRecipientState) {
        val currentRecipient = state.currentShopRecipient
        dispatch(
            Message.UpdateShopRecipientState(
                state.copy(
                    isExpanded = !state.isExpanded,
                    mutableShopRecipient = currentRecipient
                )
            )
        )
    }

    protected fun onChangeShopRecipientFields(
        state: State.ShopRecipientState,
        recipient: ShopRecipient
    ) {
        dispatch(
            Message.UpdateShopRecipientState(
                state.copy(mutableShopRecipient = recipient)
            )
        )
    }

    protected fun obtainShopOrderPrice(state: State) {
        val price = ShopOrderPrice(
            totalPrice = state.shopOrderResultItems
                .getAvailableItems()
                .sumOf { it.calculateTotalCost() },
            productCount = state.shopOrderResultItems
                .getAvailableItems()
                .sumOf { it.count },
            totalDiscount = calculateTotalDiscount(state.shopOrderResultItems.getAvailableItems())
        )
        dispatch(Message.UpdateOrderState(state.copy(shopOrderPrice = price)))
    }

    protected fun obtainCheckOrder(state: State, onGetPrice : () -> Unit) {
        scope.launch {
            dispatch(
                Message.UpdateCreateOrderStatus(
                    State.ShopCreateOrderButtonState(isLoading = true, isAvailable = false, message = "")
                )
            )
            orderRepository.checkOrder(state.shopOrderItems).fold(
                onSuccess = {
                    if (it.containsUnAvailableItems())
                        dispatch(Message.UpdateMessage(
                            triggered(
                                EventContent(
                                success = false,
                                message = "Некоторые товары не доступны для заказа " +
                                        "\n стоимость заказа была обновлена"
                            ))
                        ))
                    val isAvailable = it.isAvailableToCreateOrder()
                    dispatch(
                        Message.UpdateCreateOrderStatus(
                            State.ShopCreateOrderButtonState(
                                isLoading = false,
                                isAvailable = isAvailable,
                                message = if (isAvailable) "Оформить" else "Заказ не доступен"
                            )
                        )
                    )
                    dispatch(Message.UpdateShopOrderResultItems(it))
                    onGetPrice()
                },
                onFailure = {
                    dispatch(
                        Message.UpdateCreateOrderStatus(
                            State.ShopCreateOrderButtonState(
                                isLoading = false,
                                isAvailable = false,
                                message = "Заказ не доступен"
                            )
                        )
                    )
                    dispatch(
                        Message.UpdateMessage(
                            triggered(
                                EventContent(
                                    success = false,
                                    message = it.mapToSauceError().mapToString()
                                )
                            )
                        )
                    )
                    onGetPrice()
                }
            )
        }

    }

    protected fun onClickCreateOrder(state: State) {
        scope.launch {
            dispatch(
                Message.UpdateCreateOrderStatus(
                    state.shopCreateOrderState.copy(
                        isLoading = true
                    )
                )
            )
            orderRepository.createOrder(
                deliveryType = state.deliveryType.selectedOption,
                paymentType = state.paymentType.selectedOption,
                shopItems = state.shopOrderResultItems.getAvailableItems(),
                recipient = state.shopRecipientState.currentShopRecipient
            ).fold(
                onSuccess = {
                    dispatch(
                        Message.UpdateCreateOrderStatus(state.shopCreateOrderState.copy(isLoading = false))
                    )
                    dispatch(
                        Message.UpdateMessage(
                            triggered(
                                EventContent(success = true, message = it.order.toString())
                            )
                        )
                    )
                    intentAction.openWebPage(
                        it.getUrlAfterCreateOrder()
                    )
                    state.shopOrderResultItems.getAvailableItems().forEach {
                        cartRepository.remove(it)
                    }
                },
                onFailure = {
                    dispatch(
                        Message.UpdateCreateOrderStatus(
                            state.shopCreateOrderState.copy(isLoading = false, isAvailable = true)
                        )
                    )
                    dispatch(
                        Message.UpdateMessage(
                            triggered(
                                EventContent(
                                    success = false,
                                    message = it.message.toString()
                                )
                            )
                        )
                    )
                }
            )
        }
    }

    protected fun onConsumedCreateOrderMessage(state: ShopCreateOrderStore.State) {
        dispatch(
            Message.UpdateOrderState(
                state.copy(message = consumed())
            )
        )
    }

    /**
     * Fetch shopRecipient from auth user, use single in first launch
     */
    protected fun obtainShopRecipient(state: State) {
        scope.launch {
            val shopRecipient = orderRepository.getUserInfo()
            dispatch(
                Message.UpdateShopRecipientState(
                    state.shopRecipientState.copy(
                        mutableShopRecipient = shopRecipient,
                        currentShopRecipient = shopRecipient
                    )
                )
            )
        }
    }

    private fun calculateTotalDiscount(cart: List<ShopItemCart>): Int {
        var totalDiscount = 0
        for (item in cart) {
            if (item.numberPreviousPrice != null && item.numberPreviousPrice!! > item.numberPrice) {
                val discountPerItem = item.numberPreviousPrice!! - item.numberPrice
                totalDiscount += discountPerItem * item.count
            }
        }
        return if (totalDiscount <= 0) 0 else totalDiscount
    }
}

