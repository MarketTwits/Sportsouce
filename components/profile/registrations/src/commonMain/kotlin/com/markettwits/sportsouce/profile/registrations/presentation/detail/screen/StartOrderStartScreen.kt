package com.markettwits.sportsouce.profile.registrations.presentation.detail.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import com.markettwits.core_ui.items.components.topbar.SauceScaffold as Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.navigation.LocalScrollContentBottomPadding
import com.markettwits.core_ui.items.components.snackbar.SauceSnackbarHost
import com.markettwits.core_ui.items.components.snackbar.SauceSnackbarTone
import com.markettwits.core_ui.items.components.topbar.TopBarWithClip
import com.markettwits.core_ui.items.extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.presentation.detail.component.StartOrderComponent
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start.StartOrderTabRow
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs.OrderMembersTab
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs.OrderOverviewTab
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs.OrderPaymentTab
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs.OrderShareTab
import com.markettwits.sportsouce.profile.registrations.presentation.detail.store.StartOrderStore
import com.markettwits.sportsouce.profile.registrations.presentation.management.*
import com.markettwits.sportsouce.profile.registrations.presentation.management.RegistrationManagementComponent.Intent
import com.markettwits.sportsouce.profile.registrations.presentation.management.RegistrationManagementComponent.Sheet

@Composable
fun rememberStartOrderPagerState(orderInfo: StartOrderInfo): PagerState {
    val pageCount = remember(orderInfo.payment.isPaid, orderInfo.isRefunded) {
        if (orderInfo.payment.isPaid && !orderInfo.isRefunded) 4 else 3
    }
    return rememberPagerState(pageCount = { pageCount })
}

@Composable
fun StartOrderStartScreen(component: StartOrderComponent) {
    val state by component.state.collectAsState()
    val management = component.management
    val managementSnackbar = remember { SnackbarHostState() }
    val managementState = management?.state?.collectAsState()?.value
    val loadedOrder = state.startOrderInfo
    val order = if (managementState?.refundedOrderIds?.contains(loadedOrder.id) == true) {
        loadedOrder.copy(isRefunded = true)
    } else loadedOrder
    if (management != null && !order.isRefunded && !order.isRegistrationPast()) {
        LaunchedEffect(management, order.id, managementState?.previewGeneration) {
            management.accept(Intent.PreviewOptions(order.id))
        }
    }
    val changeDistance = {
        component.obtainEvent(StartOrderStore.Intent.OnClickReRegistration(order.startId, order.id))
    }
    val pay = {
        if (management != null) management.accept(Intent.Open(Sheet.Pay(order)))
        else component.obtainEvent(StartOrderStore.Intent.OnClickPay(order.id))
    }
    val updatePrice = { component.obtainEvent(StartOrderStore.Intent.OnClickUpdatePrice) }
    val refund = management?.takeIf { order.canRefund() }?.let { manager ->
        { manager.accept(Intent.Open(Sheet.Refund(order))) }
    }
    val registrationOptions = managementState?.optionPreviews?.get(order.id)
    val manageOptions = management?.let { manager -> { manager.accept(Intent.Open(Sheet.Options(order))) } }
    val pagerState = rememberStartOrderPagerState(order)
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val inheritedBottomPadding = LocalScrollContentBottomPadding.current

    LaunchedEffect(state.refreshError) {
        state.refreshError?.let { message ->
            snackbarHostState.showLongMessageWithDismiss(message)
            component.obtainEvent(StartOrderStore.Intent.DismissRefreshError)
        }
    }

    Scaffold(
        topBar = {
            TopBarWithClip(title = order.id.takeIf { it > 0 }?.let { "Регистрация № $it" } ?: "Детали регистрации") {
                component.obtainEvent(StartOrderStore.Intent.Dismiss)
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = {
            androidx.compose.foundation.layout.Box {
                SauceSnackbarHost(snackbarHostState, tone = SauceSnackbarTone.Error)
                SauceSnackbarHost(managementSnackbar)
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            StartOrderTabRow(
                pagerState = pagerState,
                orderInfo = order,
                priceResult = state.startPriceResult,
                coroutineScope = coroutineScope
            )
            CompositionLocalProvider(
                LocalScrollContentBottomPadding provides inheritedBottomPadding + paddingValues.calculateBottomPadding(),
            ) {
                AdaptivePane {
                    PullToRefreshScreen(
                        modifier = Modifier.fillMaxSize(),
                        isRefreshing = state.isRefreshing,
                        onRefresh = {
                            management?.accept(Intent.LoadParticipantContext)
                            management?.accept(Intent.ReloadPreviews)
                            component.obtainEvent(StartOrderStore.Intent.RefreshOrder(userInitiated = true))
                        },
                    ) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize(),
                        ) { page ->
                            when (page) {
                                0 -> OrderOverviewTab(
                                    order = order,
                                    price = state.startPriceResult,
                                    options = registrationOptions,
                                    optionsError = managementState?.optionPreviewErrors?.get(order.id),
                                    onOpenStart = order.startId.takeIf { it > 0 }?.let { id ->
                                        { component.obtainEvent(StartOrderStore.Intent.OnClickStart(id)) }
                                    },
                                    onPay = pay,
                                    onUpdatePrice = updatePrice,
                                    onHelp = { component.obtainEvent(StartOrderStore.Intent.OnClickHelp) },
                                    onChangeDistance = changeDistance,
                                    onManageOptions = manageOptions,
                                    onRefund = refund,
                                )

                                1 -> OrderMembersTab(
                                    order = order,
                                    user = managementState?.user,
                                    transfersError = managementState?.transfersError?.takeIf { order.canManageSlot() },
                                    onRetryTransfers = management?.let { { it.accept(Intent.LoadParticipantContext) } },
                                    onCorrect = management?.let { manager -> { manager.accept(Intent.Open(Sheet.Correct(it))) } },
                                    onDistance = changeDistance.takeIf { order.canManageSlot() },
                                    slotActions = management?.let { manager ->
                                        { member ->
                                            RegistrationSlotActions(
                                                member = member, state = managementState, component = manager,
                                                available = order.canManageSlot(),
                                            )
                                        }
                                    },
                                )

                                2 -> OrderPaymentTab(
                                    order = order,
                                    price = state.startPriceResult,
                                    canOpenPayment = management != null,
                                    options = registrationOptions,
                                    onManageOptions = manageOptions,
                                    onPay = pay,
                                    onUpdatePrice = updatePrice,
                                    onRefund = refund,
                                )

                                3 -> if (order.payment.isPaid && !order.isRefunded) {
                                    OrderShareTab(order)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    if (management != null) RegistrationManagementFeedback(management, managementSnackbar)
}
