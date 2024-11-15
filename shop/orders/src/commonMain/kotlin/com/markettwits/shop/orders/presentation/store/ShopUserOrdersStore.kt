package com.markettwits.shop.orders.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.shop.orders.domain.models.ShopUserOrder
import com.markettwits.shop.orders.presentation.store.ShopUserOrdersStore.Intent
import com.markettwits.shop.orders.presentation.store.ShopUserOrdersStore.Label
import com.markettwits.shop.orders.presentation.store.ShopUserOrdersStore.State

interface ShopUserOrdersStore : Store<Intent, State, Label> {

    data class State(
        val isLoading : Boolean = false,
        val isSuccess : Boolean = false,
        val sauceError: SauceError? = null,
        val items : List<ShopUserOrder> = emptyList()
    )

    sealed interface Intent{
        data object OnClickRetry : Intent
        data object OnClickGoBack : Intent
    }

    sealed interface Message{
        data object Loading : Message
        data class Loaded(val items : List<ShopUserOrder>) : Message
        data class Error(val sauceError: SauceError) : Message
    }

    sealed interface Label{
        data object GoBack : Label
    }

}
