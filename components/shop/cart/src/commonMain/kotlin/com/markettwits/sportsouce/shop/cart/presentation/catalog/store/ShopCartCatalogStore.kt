package com.markettwits.sportsouce.shop.cart.presentation.catalog.store

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.markettwits.sportsouce.shop.cart.domain.ShopCartRepository
import com.markettwits.sportsouce.shop.cart.presentation.catalog.component.ShopCartCatalogComponent
import com.markettwits.sportsouce.shop.domain.mapper.formatPriceWithSpaces
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

interface ShopCartCatalogStore : InstanceKeeper.Instance {

    val state: StateFlow<ShopCartCatalogComponent.State>

    interface Factory {
        fun create() : ShopCartCatalogStore
    }
}

class ShopCartCatalogStoreFactory(
    private val repository: ShopCartRepository
) : ShopCartCatalogStore.Factory {
    override fun create() : ShopCartCatalogStore = ShopCartCatalogStoreBase(repository)

}

private class ShopCartCatalogStoreBase(private val repository: ShopCartRepository) :
    ShopCartCatalogStore {

    private var cartState: MutableStateFlow<ShopCartCatalogComponent.State> =
        MutableStateFlow(ShopCartCatalogComponent.State.Empty)

    init {
        CoroutineScope(Dispatchers.Main.immediate).launch {

            repository.observe().map { items ->
                if (items.isEmpty()) {
                    ShopCartCatalogComponent.State.Empty
                } else {
                    val totalCost = items.sumOf { item ->
                        item.numberPrice * item.count
                    }
                    ShopCartCatalogComponent.State.Expanded(
                        cartCost = formatPriceWithSpaces(totalCost)
                    )
                }
            }.onEach { state ->
                cartState.value = state
            }.launchIn(CoroutineScope(Dispatchers.Main.immediate))
        }
    }

    override val state: StateFlow<ShopCartCatalogComponent.State> = cartState
}