package com.markettwits.sportsouce.profile.registrations.presentation.detail.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.topbar.TopBarWithClip
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.presentation.detail.component.StartOrderComponent
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start.StartOrderTabRow
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs.OrderMembersTab
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs.OrderOverviewTab
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs.OrderPaymentTab
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs.OrderShareTab
import com.markettwits.sportsouce.profile.registrations.presentation.detail.store.StartOrderStore

@Composable
fun rememberStartOrderPagerState(orderInfo: StartOrderInfo): PagerState {
    val pageCount = remember(orderInfo.payment.isPaid) {
        if (orderInfo.payment.isPaid) 4 else 3
    }
    return rememberPagerState(pageCount = { pageCount })
}

@Composable
fun StartOrderStartScreen(component: StartOrderComponent) {
    val state by component.state.collectAsState()
    val pagerState = rememberStartOrderPagerState(state.startOrderInfo)
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopBarWithClip(title = "Детали регистрации") {
                component.obtainEvent(StartOrderStore.Intent.Dismiss)
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            StartOrderTabRow(
                pagerState = pagerState,
                orderInfo = state.startOrderInfo,
                priceResult = state.startPriceResult,
                coroutineScope = coroutineScope
            )
            AdaptivePane {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    when (page) {
                        0 -> OrderOverviewTab(
                            orderInfo = state.startOrderInfo,
                            priceState = state.startPriceResult,
                            onClickStart = { component.obtainEvent(StartOrderStore.Intent.OnClickStart(it)) },
                            onClickPay = { component.obtainEvent(StartOrderStore.Intent.OnClickPay(state.startOrderInfo.id)) },
                            onUpdatePrice = { component.obtainEvent(StartOrderStore.Intent.OnClickUpdatePrice) },
                            onHelp = { component.obtainEvent(StartOrderStore.Intent.OnClickHelp) }
                        )

                        1 -> OrderMembersTab(
                            members = state.startOrderInfo.members
                        )

                        2 -> OrderPaymentTab(
                            orderInfo = state.startOrderInfo,
                            priceState = state.startPriceResult
                        )

                        3 -> if (state.startOrderInfo.payment.isPaid) {
                            OrderShareTab(state.startOrderInfo)
                        }
                    }
                }
            }
        }
    }
}


