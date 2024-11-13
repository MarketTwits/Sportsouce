package com.markettwits.shop.orders.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.shop.orders.presentation.component.ShopUserOrdersComponent

@Composable
fun ShopUserOrdersScreen(
    modifier: Modifier = Modifier,
    component: ShopUserOrdersComponent
) {
    val state by component.state.collectAsState()


}