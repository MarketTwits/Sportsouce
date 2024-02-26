package com.markettwits.registrations.registrations.presentation.components.starts

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.base_screen.PullToRefreshScreen
import com.markettwits.registrations.registrations.domain.StartOrderInfo
import com.markettwits.registrations.start_order_profile.components.start.OrderStartCard


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RegistrationsStart(
    starts: List<StartOrderInfo>,
    isRefreshing: Boolean,
    onClick: (StartOrderInfo) -> Unit,
    onRefresh: () -> Unit
) {
    PullToRefreshScreen(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh::invoke,
    ) {
        LazyColumn(modifier = it) {
            items(items = starts, key = { it -> it.id }) {
                OrderStartCard(
                    modifier = Modifier.animateItemPlacement(animationSpec = tween(600)),
                    item = it,
                    onClickStart = { start ->
                        onClick(it)
                    })
            }
        }
        if (starts.isEmpty()) {
            RegistrationsEmpty()
        }
    }
}