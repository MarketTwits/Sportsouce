package com.markettwits.sportsouce.shop.orders.presentation.components.states

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.progress.SauceSkeleton

@Composable
internal fun ShopUserOrdersLoadingContent(modifier: Modifier = Modifier, isLoading: Boolean, isListEmpty: Boolean) {
    if (isLoading && isListEmpty) Column(modifier.fillMaxSize().padding(16.dp).semantics {
        contentDescription = "Загрузка заказов"; progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
    }, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        repeat(3) { OnBackgroundCard {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                SauceSkeleton(Modifier.fillMaxWidth(0.6f).height(20.dp))
                SauceSkeleton(Modifier.fillMaxWidth().height(80.dp))
                SauceSkeleton(Modifier.width(100.dp).height(20.dp))
            }
        } }
    }
}
