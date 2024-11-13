package com.markettwits.shop.orders.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.shop.orders.presentation.store.ShopUserOrdersStore.Intent
import com.markettwits.shop.orders.presentation.store.ShopUserOrdersStore.Label
import com.markettwits.shop.orders.presentation.store.ShopUserOrdersStore.State

interface ShopUserOrdersStore : Store<Intent, State, Label> {
    object State

    sealed interface Intent

    sealed interface Message

    sealed interface Label

}
