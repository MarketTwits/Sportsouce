package com.markettwits.shop.cart.presentation.cart.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.shop.cart.domain.ShopItemCart
import com.markettwits.shop.cart.presentation.cart.store.ShopCartStore.Intent
import com.markettwits.shop.cart.presentation.cart.store.ShopCartStore.Label
import com.markettwits.shop.cart.presentation.cart.store.ShopCartStore.State

interface ShopCartStore : Store<Intent, State, Label> {

    data class State(
        val items : List<ShopItemCart> = emptyList(),
        val itemsCount : Int = 0,
        val isAvailable: Boolean = false,
    )

    sealed interface Intent{
        data class OnClickDelete(val item: ShopItemCart) : Intent
        data class OnClickIncrease(val item : ShopItemCart) : Intent
        data class OnClickDecrease(val item : ShopItemCart) : Intent
        data class OnClickItem(val item : ShopItemCart) : Intent
        data object OnClickGoBack : Intent
        data object OnClickGoAuth : Intent
        data object OnClickCreateOrder : Intent
        data object Init : Intent
    }

    sealed interface Message{
        data class UpdateItemsInCart(val items : List<ShopItemCart>,val itemsCount : Int) : Message
        data class UpdateCreateOrderAvailable(val boolean: Boolean) : Message
    }

    sealed interface Label{
        data object GoBack : Label
        data object GoAuth : Label
        data class GoOrderScreen(val items : List<ShopItemCart>) : Label
        data class GoToShopItem(val shopItemCart: ShopItemCart) : Label
    }

}
