package com.markettwits.sportsouce.shop.cart.presentation.catalog.components

import androidx.compose.material3.MaterialTheme
import com.markettwits.core_ui.items.theme.SauceTheme

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
internal fun ShopCartCatalogButton(
    modifier: Modifier = Modifier,
    price: String,
    onClick: () -> Unit
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        shape = com.markettwits.core_ui.items.theme.SauceShapes.field,
        containerColor = SauceTheme.colors.actionSoft,
        contentColor = SauceTheme.colors.onActionSoft
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(4.dp),
                imageVector = Icons.Default.ShoppingCart,
                tint = SauceTheme.colors.action,
                contentDescription = "Корзина"
            )
            Text(
                modifier = Modifier.padding(4.dp),
                text = "$price ₽",
                color = SauceTheme.colors.action,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.titleSmall,
            )
        }

    }
}
