package com.markettwits.registrations.registrations_list.presentation.components.starts

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.base_screen.PullToRefreshScreen
import com.markettwits.registrations.registrations_list.domain.StartOrderInfo
import com.markettwits.registrations.registrations_list.presentation.components.filter.FilterItem
import com.markettwits.registrations.registrations_list.presentation.components.filter.RegistrationsFilterItem
import com.markettwits.registrations.start_order_detail.components.start.OrderStartCard


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RegistrationsStart(
    starts: List<StartOrderInfo>,
    filter: List<FilterItem>,
    isRefreshing: Boolean,
    onClick: (StartOrderInfo) -> Unit,
    onClickFilter: (FilterItem) -> Unit,
    onRefresh: () -> Unit
) {
    PullToRefreshScreen(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh::invoke,
    ) {
        LazyColumn(modifier = it) {
            item {
                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    filter.forEach { filterItem ->
                        RegistrationsFilterItem(
                            modifier = Modifier.padding(10.dp),
                            value = filterItem.value,
                            checked = filterItem.checked,
                            onClick = {
                                onClickFilter(filterItem)
                            })
                    }
                }
            }
            items(items = starts, key = { it -> it.id }, contentType = { it }) {
                OrderStartCard(
                    modifier = Modifier
                        .padding(10.dp)
                        .animateItemPlacement(animationSpec = tween(600)),
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