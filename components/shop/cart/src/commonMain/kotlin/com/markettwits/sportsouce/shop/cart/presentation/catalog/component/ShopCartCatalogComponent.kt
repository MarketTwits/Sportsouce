package com.markettwits.sportsouce.shop.cart.presentation.catalog.component

import kotlinx.coroutines.flow.StateFlow

interface ShopCartCatalogComponent {

    val state : StateFlow<State>

    fun onClickCart()

    sealed interface State {
        data class Expanded(val cartCost : String) : State
        data object Empty : State
    }

}