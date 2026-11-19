package com.markettwits.sportsouce.shop.orders.presentation.components.states

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.core.errors.api.throwable.mapToString
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.theme.SauceTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.sportsouce.shop.orders.domain.models.ShopUserOrder

@Composable
internal fun ShopUserOrdersItemsContent(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    isSuccess : Boolean,
    items : List<ShopUserOrder>,
    refreshError: SauceError? = null,
    onClickRetry: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize().padding(paddingValues).consumeWindowInsets(paddingValues)
            .verticalScroll(rememberScrollState()).padding(16.dp)
    ) {
        if (refreshError != null && items.isNotEmpty()) OnBackgroundCard {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Не удалось обновить заказы", style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textPrimary)
                Text(refreshError.mapToString(), style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
                SauceButton("Повторить попытку", onClickRetry, variant = SauceButtonVariant.Outlined)
            }
        }
        if (isSuccess) {
            if (items.isNotEmpty()) {
                ShopUserOrdersWithItemsContent(items = items)
            } else {
                ShopUserOrdersWithoutItemsContent()
            }
        }
    }
}
