package com.markettwits.shop.filter.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.shop.filter.presentation.store.ShopFilterStore
import com.markettwits.shop.filter.presentation.store.ShopFilterStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ShopFilterComponentBase(
    componentContext: ComponentContext,
    private val store: ShopFilterStore,
    private val output: ShopFilterComponent.Output,
) : ShopFilterComponent,
    ComponentContext by componentContext {

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<ShopFilterStore.State> = store.stateFlow

    override fun obtainEvent(intent: ShopFilterStore.Intent) {
        store.accept(intent)
    }

    init {
        store.labels.onEach {
            when (it) {
                is ShopFilterStore.Label.ApplyFilter -> output.applyFilter(it.result)
                is ShopFilterStore.Label.GoBack -> output.goBack()
            }
        }.launchIn(CoroutineScope(Dispatchers.Main.immediate))
    }
}
