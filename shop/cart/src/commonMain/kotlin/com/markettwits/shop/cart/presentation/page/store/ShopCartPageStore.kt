package com.markettwits.shop.cart.presentation.page.store

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.markettwits.shop.cart.domain.ShopCartRepository
import com.markettwits.shop.cart.domain.ShopItemCart
import com.markettwits.shop.cart.presentation.page.component.ShopCartPageComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ShopCartPageStoreFactory(
    private val repository: ShopCartRepository
) : ShopCartPageStore.Factory {

    override fun create(shopItemCart: ShopItemCart): ShopCartPageStore {
        return ShopCartPageStoreBase(repository, shopItemCart)
    }
}

interface ShopCartPageStore : InstanceKeeper.Instance {

    val state: StateFlow<ShopCartPageComponent.State>

    fun add()

    fun remove()

    fun updateItem(shopItemCart: ShopItemCart)

    interface Factory {
        fun create(shopItemCart: ShopItemCart): ShopCartPageStore
    }
}

private class ShopCartPageStoreBase(
    private val repository: ShopCartRepository,
    initialShopItemCart: ShopItemCart
) : ShopCartPageStore {

    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    override val state =
        MutableStateFlow<ShopCartPageComponent.State>(ShopCartPageComponent.State.Empty)

    private val currentShopItemCart = MutableStateFlow(initialShopItemCart)

    init {
        observe()
    }

    override fun add() {
        scope.launch {
            if (currentShopItemCart.value.quantity > currentShopItemCart.value.count) {
                repository.add(currentShopItemCart.value)
            }
        }
    }

    override fun remove() {
        scope.launch {
            repository.remove(currentShopItemCart.value)
        }
    }

    override fun updateItem(shopItemCart: ShopItemCart) {
        currentShopItemCart.value = shopItemCart
        observe()
    }

    private fun observe() {
        repository.observe()
            .map { items ->
                runCatching {
                    items.first { it.uuid == currentShopItemCart.value.uuid }
                }
            }
            .onEach { item ->
                item.fold(onSuccess = {
                    state.value = ShopCartPageComponent.State.InCart(
                        count = it.count.toString(),
                        quantity = it.quantity
                    )
                }, onFailure = {
                    state.value = ShopCartPageComponent.State.Empty
                })
            }.launchIn(scope)
    }

}