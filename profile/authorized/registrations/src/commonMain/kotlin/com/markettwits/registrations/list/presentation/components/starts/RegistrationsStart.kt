package com.markettwits.registrations.list.presentation.components.starts

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.base_screen.PullToRefreshScreen
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.registrations.detail.components.start.OrderStartCard
import com.markettwits.registrations.list.domain.StartOrderInfo
import com.markettwits.registrations.list.domain.firstUnpaidItem
import com.markettwits.registrations.list.presentation.components.filter.FilterItem
import com.markettwits.registrations.list.presentation.components.filter.RegistrationsFilterItem


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
        val state = rememberLazyListState()
        LazyColumn(
            state = state,
            modifier = it
        ) {
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
            item {
                starts.firstUnpaidItem()?.let { startOrderInfo ->
                    RegistrationsShouldBePay(
                        modifier = Modifier.padding(10.dp)
                    ) {
                        onClick(startOrderInfo)
                    }
                }
            }
            item {
                if (starts.isEmpty()) {
                    RegistrationsAbsolutelyEmpty(modifier = Modifier.padding(10.dp))
                }
            }
            if (starts.isNotEmpty()) {
                item {
                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        text = "Всего ${starts.size}",
                        fontSize = 14.sp,
                        fontFamily = FontNunito.semiBoldBold(),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.outline
                    )
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
        }

    }
}