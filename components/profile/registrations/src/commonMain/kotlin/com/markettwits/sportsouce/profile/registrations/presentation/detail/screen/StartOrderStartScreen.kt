package com.markettwits.sportsouce.profile.registrations.presentation.detail.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.topbar.TopBarWithClip
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.profile.registrations.presentation.detail.component.StartOrderComponent
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start.PulsingIndicator
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs.OrderMembersTab
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs.OrderOverviewTab
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs.OrderPaymentTab
import com.markettwits.sportsouce.profile.registrations.presentation.detail.store.StartOrderStore
import kotlinx.coroutines.launch

@Composable
fun StartOrderStartScreen(component: StartOrderComponent) {
    val state by component.state.collectAsState()
    val pagerState = rememberPagerState(pageCount = { 3 })
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
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.tertiary,
                indicator = { tabPositions ->
                    if (pagerState.currentPage < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                            color = MaterialTheme.colorScheme.tertiary,
                        )
                    }
                }
            ) {
                Tab(
                    selected = pagerState.currentPage == 0,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(0) } },
                    text = {
                        Box(contentAlignment = Alignment.CenterStart) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "Обзор",
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                    fontSize = 14.sp,
                                    color = if (pagerState.currentPage == 0) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.outline,
                                    fontFamily = FontNunito.bold(),
                                )
                                if (state.startPriceResult is StartOrderStore.StartPriceResult.Success &&
                                    !state.startOrderInfo.payment.isPaid
                                ) {
                                    PulsingIndicator(
                                        color = MaterialTheme.colorScheme.onErrorContainer,
                                        modifier = Modifier.padding(start = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                )
                Tab(
                    selected = pagerState.currentPage == 1,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(1) } },
                    text = {
                        Box(contentAlignment = Alignment.CenterStart) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "Участники",
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                    fontSize = 14.sp,
                                    color = if (pagerState.currentPage == 1) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.outline,
                                    fontFamily = FontNunito.bold(),
                                )
                                if (state.startOrderInfo.members.any { it.results.isNotEmpty() }) {
                                    PulsingIndicator(
                                        color = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.padding(start = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                )
                Tab(
                    selected = pagerState.currentPage == 2,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(2) } },
                    text = {
                        Text(
                            text = "Оплата",
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = if (pagerState.currentPage == 2) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.outline,
                            fontFamily = FontNunito.bold(),
                        )
                    }
                )
            }
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
                    }
                }
            }
        }
    }
}
