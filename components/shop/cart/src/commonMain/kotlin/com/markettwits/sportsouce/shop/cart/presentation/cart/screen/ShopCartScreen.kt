package com.markettwits.sportsouce.shop.cart.presentation.cart.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.components.navigation.SauceActionFooter
import com.markettwits.core_ui.items.components.navigation.SauceLazyColumn as LazyColumn
import com.markettwits.core_ui.items.components.topbar.SauceScaffold as Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.topbar.TopBarBase
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.sportsouce.shop.cart.presentation.cart.component.ShopCartComponent
import com.markettwits.sportsouce.shop.cart.presentation.cart.components.CreateOrderButton
import com.markettwits.sportsouce.shop.cart.presentation.cart.components.ShopCartItems

@Composable
fun ShopCartScreen(
    modifier: Modifier = Modifier,
    component: ShopCartComponent
) {

    val state by component.state.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopBarBase(title = "Корзина", goBack = component::onClickGoBack)
        },
        bottomBar = {
            if (state.items.isNotEmpty()) SauceActionFooter {
                AdaptivePane(fillHeight = false) {
                    CreateOrderButton(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                        onClickCreateOrder = component::onClickCreateOrder,
                        onClickGoAuth = component::onClickGoAuth,
                        isAvailable = state.isAvailable,
                    )
                }
            }
        },
    ) { paddingValues ->
        AdaptivePane {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues).consumeWindowInsets(paddingValues),
                contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ShopCartItems(
                    items = state.items,
                    onClickDecrease = component::onClickDecrease,
                    onClickIncrease = component::onClickIncrease,
                    onClickItem = component::onClickShopCartItem,
                    onClickDelete = component::onClickDelete
                )
                if (state.items.isEmpty()) item {
                    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("Корзина пуста", style = MaterialTheme.typography.titleMedium, color = SauceTheme.colors.textPrimary)
                        Text("Добавьте товары из каталога", style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
                        SauceButton("Продолжить покупки", component::onClickGoBack)
                    }
                }
            }
        }
    }
}
