package com.markettwits.sportsouce.shop.cart.presentation.cart.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.buttons.SauceButton

@Composable
fun CreateOrderButton(modifier: Modifier = Modifier, isAvailable: Boolean, onClickCreateOrder: () -> Unit, onClickGoAuth: () -> Unit) {
    SauceButton(
        if (isAvailable) "Оформить заказ" else "Войти для оформления",
        { if (isAvailable) onClickCreateOrder() else onClickGoAuth() }, modifier)
}
