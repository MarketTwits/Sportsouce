package com.markettwits.shop.search.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.shop.search.presentation.store.ShopSearchStore
import com.markettwits.shop.search.presentation.store.ShopSearchStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ShopSearchComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: ShopSearchStoreFactory,
    private val outputs: ShopSearchComponent.Outputs,
    query : String = "",
) : ShopSearchComponent, ComponentContext by componentContext {

    private val store = storeFactory.create(query)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<ShopSearchStore.State> = store.stateFlow

    override fun obtainEvent(intent: ShopSearchStore.Intent) {
        store.accept(intent)
    }

    init {
        store.labels.onEach {
            when(it){
                is ShopSearchStore.Label.GoBack -> outputs.goBack()
                is ShopSearchStore.Label.OnApplyQuery -> outputs.onApplyQuery(it.query)
            }
        }.launchIn(CoroutineScope(Dispatchers.Main.immediate))
    }

}