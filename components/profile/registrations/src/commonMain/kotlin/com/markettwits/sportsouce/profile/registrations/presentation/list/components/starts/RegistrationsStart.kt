package com.markettwits.sportsouce.profile.registrations.presentation.list.components.starts

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start.OrderStartCard
import com.markettwits.sportsouce.profile.registrations.presentation.list.components.filter.FilterItem
import com.markettwits.sportsouce.profile.registrations.presentation.list.components.filter.RegistrationsEmptyFiltered

@Composable
fun RegistrationsStart(
    modifier: Modifier = Modifier,
    withoutFilterStarts: List<StartOrderInfo>,
    withFilterStarts: List<StartOrderInfo>,
    filter: List<FilterItem>,
    onClick: (StartOrderInfo) -> Unit,
) {
    val gridState = rememberLazyGridState()
    val activeFiltersCount = filter.count { it.checked }

        AdaptivePane {
            LazyVerticalGrid(
                state = gridState,
                modifier = modifier,
                columns = GridCells.Adaptive(minSize = 320.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                if (withoutFilterStarts.isNotEmpty()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = "Всего ${withFilterStarts.size} результатов",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        )

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


