package com.markettwits.sportsouce.profile.registrations.presentation.list.components.starts

import com.markettwits.core_ui.items.components.navigation.SauceLazyVerticalGrid as LazyVerticalGrid

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.theme.SauceSpacing
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.sportsouce.auth.service.api.SharedUser
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
    ownerFiltered: Boolean = false,
    user: SharedUser? = null,
    header: (@Composable () -> Unit)? = null,
    orderExtras: (@Composable (StartOrderInfo) -> Unit)? = null,
    gridState: LazyGridState = rememberLazyGridState(),
) {
    val activeFiltersCount = filter.count { it.checked }

    LazyVerticalGrid(
        state = gridState,
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = 320.dp * LocalDensity.current.fontScale),
        contentPadding = PaddingValues(horizontal = SauceSpacing.large, vertical = SauceSpacing.medium),
        horizontalArrangement = Arrangement.spacedBy(SauceSpacing.medium),
        verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium),
    ) {
        if (header != null) item(span = { GridItemSpan(maxLineSpan) }) { header() }
        if (withoutFilterStarts.isNotEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    modifier = Modifier.padding(bottom = SauceSpacing.extraSmall),
                    text = "Регистрации: ${withFilterStarts.size}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SauceTheme.colors.textSecondary,
                )

            }
        }

        if (withoutFilterStarts.isEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                RegistrationsAbsolutelyEmpty(
                    modifier = Modifier
                )
            }
        } else if (withFilterStarts.isEmpty() && (activeFiltersCount > 0 || ownerFiltered)) {
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
                    user = user,
                    extraContent = orderExtras?.let { { it(orderInfo) } },
                    onClickStart = { _ ->
                        onClick(orderInfo)
                    }
                )
            }
        }

    }
}
