package com.markettwits.sportsouce.shop.order.presentation.components.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.buttons.SauceButton

@Composable
internal fun ShopCreateOrderButton(modifier: Modifier = Modifier, isLoading: Boolean, isAvailable: Boolean,
    message: String, onClick: () -> Unit) {
    SauceButton(message, onClick, modifier, enabled = isAvailable, loading = isLoading)
}
