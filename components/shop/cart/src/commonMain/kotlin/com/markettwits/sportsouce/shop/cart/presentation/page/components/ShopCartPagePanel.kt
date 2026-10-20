package com.markettwits.sportsouce.shop.cart.presentation.page.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import com.markettwits.core_ui.items.components.navigation.SauceActionFooter
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.commerce.SauceQuantityStepper
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
internal fun ShopCartItemEmpty(modifier: Modifier = Modifier, onClickAddToCart: () -> Unit, enabled: Boolean = true, available: Boolean = true) {
    SauceActionFooter {
        SauceButton(if (available) "В корзину" else "Нет в наличии", onClickAddToCart, modifier.padding(horizontal = 16.dp, vertical = 12.dp), enabled = enabled)
    }
}

@Composable
internal fun ShopCartItemExpanded(modifier: Modifier = Modifier, countInCart: Int, quantity: Int,
    isIncreaseAvailable: Boolean, enabled: Boolean = true, onClickCart: () -> Unit, onClickAddToCart: () -> Unit, onClickRemoveFromCart: () -> Unit) {
    SauceActionFooter {
        Column(modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (quantity <= countInCart) Text("Достигнуто доступное количество", style = MaterialTheme.typography.bodySmall,
                color = SauceTheme.colors.textSecondary)
            BoxWithConstraints(Modifier.fillMaxWidth()) {
                if (maxWidth < 320.dp * LocalDensity.current.fontScale) Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SauceButton("В корзине", onClickCart, variant = SauceButtonVariant.Soft)
                    SauceQuantityStepper(countInCart, onClickRemoveFromCart, onClickAddToCart, canIncrease = isIncreaseAvailable, canDecrease = enabled && countInCart > 0)
                } else Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    SauceButton("В корзине", onClickCart, Modifier.weight(1f), variant = SauceButtonVariant.Soft)
                    SauceQuantityStepper(countInCart, onClickRemoveFromCart, onClickAddToCart, canIncrease = isIncreaseAvailable, canDecrease = enabled && countInCart > 0)
                }
            }
        }
    }
}
