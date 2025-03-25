package com.markettwits.sportsouce.shop.cart.presentation.cart.component


import com.markettwits.sportsouce.shop.cart.domain.ShopItemCart
import com.markettwits.sportsouce.shop.cart.presentation.cart.store.ShopCartStore
import kotlinx.coroutines.flow.StateFlow

interface ShopCartComponent {

    val state : StateFlow<ShopCartStore.State>

    fun onClickIncrease (item : ShopItemCart)

    fun onClickDecrease (item : ShopItemCart)

    fun onClickDelete (item: ShopItemCart)

    fun onClickShopCartItem(item : ShopItemCart)

    fun onClickGoBack()

    fun onClickCreateOrder()

    fun onClickGoAuth()


    interface Outputs {

        fun goAuth()

        fun goBack()

        fun goShopItem(shopItemCart: ShopItemCart)

        fun goOrder(items : List<ShopItemCart>)

    }

}