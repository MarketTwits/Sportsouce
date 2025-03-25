package com.markettwits.sportsouce.shop.cart.presentation.catalog.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.sportsouce.shop.cart.presentation.catalog.component.ShopCartCatalogComponent

@Composable
fun ShopCartCatalogWidget(
    modifier: Modifier = Modifier,
    component: ShopCartCatalogComponent
) {
    val state by component.state.collectAsState()
    when (state) {
        is ShopCartCatalogComponent.State.Empty -> {}
        is ShopCartCatalogComponent.State.Expanded -> ShopCartCatalogButton(
            modifier = modifier,
            price = (state as ShopCartCatalogComponent.State.Expanded).cartCost,
            onClick = component::onClickCart
        )
    }
}