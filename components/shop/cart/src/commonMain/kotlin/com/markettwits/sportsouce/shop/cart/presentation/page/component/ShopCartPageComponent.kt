package com.markettwits.sportsouce.shop.cart.presentation.page.component


import com.markettwits.sportsouce.shop.cart.domain.ShopItemCart
import kotlinx.coroutines.flow.StateFlow

interface ShopCartPageComponent {
    val state : StateFlow<State>

    fun updateItem(shopItemCart: ShopItemCart)

    fun onClickAddToCart()

    fun onClickRemove()

    fun onClickCart()

    sealed interface State{
        data class InCart(
            val count : String,
            val quantity : Int,
            val isIncreaseAvailable: Boolean,
        ) : State
        data object Empty : State
    }
    interface Outputs{
        fun goToCart()
    }
}