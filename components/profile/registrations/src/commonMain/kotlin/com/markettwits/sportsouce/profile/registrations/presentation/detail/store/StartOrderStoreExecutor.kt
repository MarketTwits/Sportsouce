package com.markettwits.sportsouce.profile.registrations.presentation.detail.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.IntentAction
import com.markettwits.core.log.LogTagProvider
import com.markettwits.core.log.errorLog
import com.markettwits.sportsouce.profile.registrations.data.StartOrderRegistrationRepository
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderType
import com.markettwits.sportsouce.profile.registrations.presentation.management.inlinePaymentAvailable
import com.markettwits.sportsouce.profile.registrations.presentation.management.registrationError
import com.markettwits.sportsouce.profile.registrations.presentation.detail.store.StartOrderStore.*
import com.markettwits.sportsouce.profile.registrations.presentation.detail.store.StartOrderStore.Label.OnClickStart
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch

class StartOrderStoreExecutor(
    private val repository: StartOrderRegistrationRepository,
    private val intentAction: IntentAction
) : CoroutineExecutor<Intent, Unit, State, Message, Label>(), LogTagProvider {

    override val tag: String = "StartOrderStoreExecutor (Detail)"
    private var refreshJob: Job? = null
    private var priceJob: Job? = null
    private var paymentJob: Job? = null
    private var pendingPaymentLink: Pair<Int, String>? = null
    private var loadedOrder: StartOrderInfo? = null
    private var followReRegistration = false

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.Dismiss -> publish(Label.Dismiss)
            is Intent.RefreshOrder -> refreshOrder(intent.userInitiated)
            Intent.CancelOrderRefresh -> {
                refreshJob?.cancel()
                dispatch(Message.RefreshStatus(false))
            }
            Intent.DismissRefreshError -> if (state().refreshError != null) dispatch(Message.RefreshStatus(state().isRefreshing))
            is Intent.OnClickPay -> getPaymentUrl(intent.orderId)
            is Intent.OnClickStart -> publish(OnClickStart(intent.startId))
            is Intent.OnClickUpdatePrice -> getActualPrice(state().startOrderInfo)
            is Intent.OnClickHelp -> {
                val opened = runCatching { intentAction.tryOpenWebPage("https://t.me/sportsoyuznsk") }.getOrDefault(false)
                if (!opened) dispatch(Message.RefreshStatus(state().isRefreshing, "Не удалось открыть ссылку на поддержку."))
            }
            is Intent.OnClickReRegistration -> {
                followReRegistration = true
                publish(Label.OnClickReRegistration(intent.startId, intent.orderId))
            }
        }
    }

    override fun executeAction(action: Unit) {
        val order = state().startOrderInfo
        loadedOrder = order
        getActualPrice(order)
    }

    private fun refreshOrder(userInitiated: Boolean) {
        if (state().isRefreshing) return
        refreshJob?.cancel()
        if (userInitiated) dispatch(Message.RefreshStatus(true))
        refreshJob = scope.launch {
            try {
                var found = false
                repository.registrations(
                    forced = userInitiated || followReRegistration,
                    allowCachedOnError = !userInitiated,
                ).collect { orders ->
                    ensureActive()
                    val current = state().startOrderInfo
                    val sameStartOrders = orders.filter { it.startId == current.startId }
                    val replacement = if (followReRegistration) {
                        sameStartOrders
                            .filter { (it.orderType as? StartOrderType.ReRegistration)?.prevGroupId == current.id }
                            .maxByOrNull { it.id }
                    } else {
                        null
                    }
                    if (replacement != null) followReRegistration = false
                    val refreshed = replacement ?: sameStartOrders.firstOrNull { it.id == current.id } ?: return@collect
                    found = true
                    val updated = refreshed.copy(
                        image = refreshed.image.ifBlank { current.image },
                        dateEndCloud = refreshed.dateEndCloud ?: current.dateEndCloud,
                        dateStartPreview = if (refreshed.dateEndCloud == null && current.dateEndCloud != null) {
                            current.dateStartPreview
                        } else {
                            refreshed.dateStartPreview
                        },
                    )
                    val orderChanged = updated != loadedOrder
                    if (orderChanged) {
                        loadedOrder = updated
                        dispatch(Message.OrderUpdated(updated))
                    }
                    if (orderChanged || (userInitiated && !updated.payment.isPaid)) {
                        val price = getActualPrice(updated)
                        if (userInitiated) price.join()
                    }
                }
                if (userInitiated) dispatch(Message.RefreshStatus(false, if (found) null else "Эта регистрация больше недоступна."))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                ensureActive()
                if (userInitiated) dispatch(Message.RefreshStatus(false, e.registrationError()))
                errorLog(e) { "Failed to refresh order ${state().startOrderInfo.id}" }
            }
        }
    }

    private fun getActualPrice(orderInfo: StartOrderInfo): Job {
        priceJob?.cancel()
        val job = scope.launch {
            if (!orderInfo.inlinePaymentAvailable()) {
                dispatch(Message.GetPriceDontRequired)
            } else {
                dispatch(Message.GetPriceLoading)
                val result = repository.getPrice(
                    orderInfo.id,
                    orderInfo.members.map { it.distanceName },
                    orderInfo.startId
                )
                ensureActive()
                result.fold(onSuccess = {
                    if (it.isRequired) dispatch(Message.GetPriceSuccess(it.totalPrice))
                    else dispatch(Message.GetPriceDontRequired)
                }, onFailure = {
                    dispatch(Message.GetPriceFailed(it.registrationError()))
                    errorLog(it) { "Failed when try get price for order ${orderInfo.id}" }
                })
            }
        }
        priceJob = job
        return job
    }

    private fun getPaymentUrl(id: Int) {
        if (paymentJob?.isActive == true || state().startOrderInfo.id != id) return
        paymentJob = scope.launch {
            dispatch(Message.GetPriceLoading)
            val cachedLink = pendingPaymentLink?.takeIf { it.first == id }?.second
            val result = if (cachedLink != null) Result.success(cachedLink) else repository.pay(id)
            ensureActive()
            if (state().startOrderInfo.id != id) return@launch
            result.fold(
                onSuccess = { url ->
                    if (url.isBlank()) {
                        dispatch(Message.GetPriceDontRequired)
                        refreshOrder(userInitiated = false)
                    } else {
                        val opened = runCatching { intentAction.tryOpenWebPage(url) }.getOrDefault(false)
                        pendingPaymentLink = if (opened) null else id to url
                        if (opened) dispatch(Message.GetPriceSuccess(state().startOrderInfo.cost))
                        else dispatch(Message.GetPriceFailed("Не удалось открыть страницу оплаты. Попробуйте ещё раз."))
                    }
                },
                onFailure = { dispatch(Message.GetPriceFailed(it.registrationError())) },
            )
        }
    }
}
