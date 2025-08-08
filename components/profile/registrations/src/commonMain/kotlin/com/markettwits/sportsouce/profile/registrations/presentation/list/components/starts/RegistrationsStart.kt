package com.markettwits.sportsouce.profile.registrations.presentation.list.components.starts

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.checkbox.FilterChipBase
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start.OrderStartCard
import com.markettwits.sportsouce.profile.registrations.presentation.list.components.filter.FilterItem
import com.markettwits.sportsouce.profile.registrations.presentation.list.components.filter.RegistrationsEmptyFiltered

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
    val gridState = rememberLazyGridState()
    val activeFiltersCount = filter.count { it.checked }

    PullToRefreshScreen(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh::invoke,
    ) { innerModifier ->
        AdaptivePane {
            LazyVerticalGrid(
                state = gridState,
                modifier = innerModifier,
                columns = GridCells.Adaptive(minSize = 320.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (filter.isNotEmpty()) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FilterList,
                                        contentDescription = "Фильтры",
                                        modifier = Modifier.size(20.dp),
                                        tint = MaterialTheme.colorScheme.secondary
                                    )
                                    Text(
                                        text = "Фильтры",
                                        fontSize = 16.sp,
                                        fontFamily = FontNunito.semiBoldBold(),
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                    if (activeFiltersCount > 0) {
                                        Badge(
                                            containerColor = MaterialTheme.colorScheme.secondary,
                                            contentColor = MaterialTheme.colorScheme.onSecondary
                                        ) {
                                            Text(
                                                text = "$activeFiltersCount",
                                                fontSize = 12.sp,
                                                fontFamily = FontNunito.bold()
                                            )
                                        }
                                    }
                                }

                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    contentPadding = PaddingValues(horizontal = 4.dp)
                                ) {
                                    items(filter) { filterItem ->
                                        FilterChipBase(
                                            label = filterItem.value,
                                            selected = filterItem.checked,
                                            onClick = {
                                                onClickFilter(filterItem)
                                            }
                                        )
//                                        RegistrationsFilterItem(
//                                            value = filterItem.value,
//                                            checked = filterItem.checked,
//                                            onClick = {
//                                                onClickFilter(filterItem)
//                                            }
//                                        )
                                    }
                                }
                            }
                        }

                        if (withoutFilterStarts.isNotEmpty()) {
                            Surface(
                                shape = MaterialTheme.shapes.small,
                                color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
                            ) {
                                Text(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    text = buildString {
                                        append("Показано ${withFilterStarts.size}")
                                        if (withFilterStarts.size != withoutFilterStarts.size) {
                                            append(" из ${withoutFilterStarts.size}")
                                        }
                                        append(" регистраций")
                                    },
                                    fontSize = 14.sp,
                                    fontFamily = FontNunito.medium(),
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            }
                        }
                    }
                }

                if (withoutFilterStarts.isEmpty()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        RegistrationsAbsolutelyEmpty(
                            modifier = Modifier
                        )
                    }
                } else if (withFilterStarts.isEmpty() && activeFiltersCount > 0) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        RegistrationsEmptyFiltered(
                            modifier = Modifier
                        )
                    }
                } else {
                    items(
                        items = withFilterStarts,
                        key = { it.id },
                    ) { orderInfo ->
                        OrderStartCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItem(fadeInSpec = tween(600)),
                            item = orderInfo,
                            onClickStart = { _ ->
                                onClick(orderInfo)
                            }
                        )
                    }
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}


