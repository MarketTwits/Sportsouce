package com.markettwits.club.dashboard.presentation.dashboard.components.subscriptions

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.club.dashboard.presentation.dashboard.store.SubscriptionsUi
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.components.buttons.ButtonContentBase

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun SubscriptionCategoriesContent(
    modifier: Modifier = Modifier,
    subscriptions: List<SubscriptionsUi>,
    onClick: (SubscriptionsUi) -> Unit
) {
    FlowRow(
        modifier = modifier,
        maxItemsInEachRow = 3
    ) {
        subscriptions.forEach {
            SubscriptionButton(
                modifier = Modifier
                    .padding(10.dp)
                    .weight(1f),
                isSelected = it.isSelected,
                title = it.title,
                onClick = { onClick(it) }
            )
        }
    }
}

@Composable
private fun SubscriptionButton(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    title: String,
    onClick: () -> Unit
) {
    val containerColor =
        if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline
    ButtonContentBase(
        modifier = modifier,
        containerColor = containerColor,
        textColor = Color.White,
        title = title,
        onClick = {
            onClick()
        },
        shape = Shapes.small
    )
}