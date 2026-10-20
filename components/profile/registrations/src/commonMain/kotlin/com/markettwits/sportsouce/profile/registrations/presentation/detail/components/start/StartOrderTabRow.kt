package com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import com.markettwits.core_ui.items.components.topbar.SauceTopBarSection
import com.markettwits.core_ui.items.components.selection.SauceScrollableTabs
import com.markettwits.core_ui.items.theme.SauceTheme
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
    val labels = listOf("Обзор", "Участники", "Оплата") + if (orderInfo.payment.isPaid) listOf("Поделиться") else emptyList()
    SauceTopBarSection {
        SauceScrollableTabs(
            labels = labels,
            selectedIndex = pagerState.currentPage.coerceIn(labels.indices),
            onSelected = { index -> coroutineScope.launch { pagerState.animateScrollToPage(index) } },
            trailingContent = { index ->
                if (index == 0 && priceResult is StartOrderStore.StartPriceResult.Success && !orderInfo.payment.isPaid) {
                    PulsingIndicator(color = SauceTheme.colors.warning.content)
                } else if (index == 1 && orderInfo.members.any { it.results.isNotEmpty() }) {
                    PulsingIndicator(color = SauceTheme.colors.action)
                }
            },
        )
    }
}
