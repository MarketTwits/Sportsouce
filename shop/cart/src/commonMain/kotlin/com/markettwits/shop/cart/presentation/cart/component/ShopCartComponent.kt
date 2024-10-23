package com.markettwits.shop.cart.presentation.cart.component


import com.markettwits.shop.cart.domain.ShopItemCart
import com.markettwits.shop.cart.presentation.cart.store.ShopCartStore
import kotlinx.coroutines.flow.StateFlow

interface ShopCartComponent {

    val state : StateFlow<ShopCartStore.State>

    fun onClickIncrease (item : ShopItemCart)

    fun onClickDecrease (item : ShopItemCart)

    fun onClickShopCartItem(item : ShopItemCart)

    fun onClickGoBack()

    fun onClickChangePaymentType()

    fun onClickCreateOrder()

    fun onClickChangeDeliveryWay()

    interface Outputs {

        fun goBack()

        fun goShopItem(shopItemCart: ShopItemCart)

    }

}