package com.markettwits.shop.cart.presentation.cart.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.shop.cart.domain.ShopItemCart
import com.markettwits.shop.cart.presentation.cart.store.ShopCartStore.Intent
import com.markettwits.shop.cart.presentation.cart.store.ShopCartStore.Label
import com.markettwits.shop.cart.presentation.cart.store.ShopCartStore.State

interface ShopCartStore : Store<Intent, State, Label> {

    data class State(
        val items : List<ShopItemCart> = emptyList(),
        val order : Order = Order(),
    ){
        data class Order(
            val payByCache : Boolean = false,
            val itemsCount : String = "",
            val totalCost : String = "",
            val discount : String = ""
        )
    }

    sealed interface Intent{
        data class OnClickIncrease(val item : ShopItemCart) : Intent
        data class OnClickDecrease(val item : ShopItemCart) : Intent
        data class OnClickItem(val item : ShopItemCart) : Intent
        data object OnClickGoBack : Intent
        data object OnClickChangePaymentType : Intent
        data object OnClickCreateOrder : Intent
        data object Init : Intent
    }

    sealed interface Message{
        data class UpdateItemsInCart(val items : List<ShopItemCart>) : Message
        data class UpdateOrder(val order: State.Order) : Message
    }

    sealed interface Label{
        data object GoBack : Label
        data class GoToShopItem(val shopItemCart: ShopItemCart) : Label
    }

}
