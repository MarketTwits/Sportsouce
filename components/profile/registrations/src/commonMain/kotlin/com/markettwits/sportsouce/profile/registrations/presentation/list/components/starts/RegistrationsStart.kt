package com.markettwits.sportsouce.profile.registrations.presentation.list.components.starts

import androidx.compose.animation.core.tween
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
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
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start.OrderStartCard
import com.markettwits.sportsouce.profile.registrations.presentation.list.components.filter.FilterItem
import com.markettwits.sportsouce.profile.registrations.presentation.list.components.filter.RegistrationsFilterItem

@Composable
fun RegistrationsStart(
    withoutFilterStarts: List<StartOrderInfo>,
    withFilterStarts: List<StartOrderInfo>,
    filter: List<FilterItem>,
    isRefreshing: Boolean,
    onClick: (StartOrderInfo) -> Unit,
    onClickFilter: (FilterItem) -> Unit,
    onRefresh: () -> Unit
) {
    val state = rememberLazyListState()

    PullToRefreshScreen(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh::invoke,
    ) { innerModifier ->
        AdaptivePane {
            LazyColumn(
                state = state,
                modifier = innerModifier
            ) {
                item {
                    Column {
                        Row(
                            modifier = Modifier
                                .horizontalScroll(rememberScrollState())
                        ) {
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
                        if (withoutFilterStarts.isEmpty()) {
                            RegistrationsAbsolutelyEmpty(
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
                items(items = withFilterStarts, key = { it.id }) {
                    if (withoutFilterStarts.isNotEmpty()) {
                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            text = "Всего ${withoutFilterStarts.size}",
                            fontSize = 14.sp,
                            fontFamily = FontNunito.semiBoldBold(),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.outline
                        )
                        withFilterStarts.forEach {
                            OrderStartCard(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .animateItem(fadeInSpec = tween(600)),
                                item = it,
                                onClickStart = { startId ->
                                    onClick(it)
                                })
                        }
                    }
                }
            }
        }
    }
}