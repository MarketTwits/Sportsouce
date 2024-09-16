package com.markettwits.shop.item.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.markettwits.shop.item.presentation.component.ShopItemPageComponent
import com.markettwits.shop.item.presentation.components.ShopItemPageContent
import com.markettwits.shop.item.presentation.store.ShopItemPageStore


@Composable
fun ShopItemPageScreen(component: ShopItemPageComponent) {

    val state by component.state.collectAsState()

    ShopItemPageContent(
        state = state, onClickOption = {
            component.obtainEvent(ShopItemPageStore.Intent.OnClickOption(it))
        }, onClickRetry = {

        }, onClickGoBack = {

        }
    )

}