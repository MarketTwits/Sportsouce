package com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.presentation.detail.store.StartOrderStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun StartOrderTabRow(
    pagerState: PagerState,
    orderInfo: StartOrderInfo,
    priceResult: StartOrderStore.StartPriceResult,
    coroutineScope: CoroutineScope,
) {
    val showShareTab = orderInfo.payment.isPaid

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
                        if (priceResult is StartOrderStore.StartPriceResult.Success &&
                            !orderInfo.payment.isPaid
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
                        if (orderInfo.members.any { it.results.isNotEmpty() }) {
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
        if (showShareTab) {
            Tab(
                selected = pagerState.currentPage == 3,
                onClick = { coroutineScope.launch { pagerState.animateScrollToPage(3) } },
                text = {
                    Text(
                        text = "Поделиться",
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = if (pagerState.currentPage == 3) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.outline,
                        fontFamily = FontNunito.bold(),
                    )
                }
            )
        }
    }
}