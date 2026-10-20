package com.markettwits.sportsouce.shop.catalog.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.essenty.backhandler.BackHandler
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.sportsouce.bottom_bar.component.listener.BottomBarVisibilityListener
import com.markettwits.sportsouce.shop.catalog.presentation.store.ShopCatalogStore
import com.markettwits.sportsouce.shop.catalog.presentation.store.ShopCatalogStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ShopCatalogComponentBase(
    componentContext: ComponentContext,
    override val listener: BottomBarVisibilityListener,
    private val storeFactory: ShopCatalogStoreFactory,
    initialFilter: com.markettwits.sportsouce.shop.filter.domain.models.ShopFilterResult? = null,
    private val outputs: ShopCatalogComponent.Outputs,
) : ShopCatalogComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        storeFactory.create(initialFilter)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<ShopCatalogStore.State> = store.stateFlow

    override fun obtainEvent(intent: ShopCatalogStore.Intent) {
        store.accept(intent)
    }

    init {
        bindShopFilterBackHandler(backHandler, lifecycle, state) {
            store.accept(ShopCatalogStore.Intent.OnClickFilter)
        }
        store.labels.onEach {
            when (it) {
                is ShopCatalogStore.Label.GoBack -> outputs.goBack()
                is ShopCatalogStore.Label.OnClickItem -> outputs.onClickShopItem(it.item)
                is ShopCatalogStore.Label.GoFilter -> outputs.goFilter()
                is ShopCatalogStore.Label.GoSearch -> outputs.goSearch(it.query)
            }
        }.launchIn(CoroutineScope(Dispatchers.Main.immediate))

    }
}

/** The filter is an overlay; Back closes it before the catalogue's parent navigation. */
internal fun bindShopFilterBackHandler(
    backHandler: BackHandler,
    lifecycle: Lifecycle,
    state: StateFlow<ShopCatalogStore.State>,
    onClose: () -> Unit,
) {
    val callback = BackCallback(isEnabled = state.value.isShowFilter, priority = BackCallback.PRIORITY_MAX, onBack = onClose)
    backHandler.register(callback)
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    state.onEach { callback.isEnabled = it.isShowFilter }.launchIn(scope)
    lifecycle.doOnDestroy {
        scope.cancel()
        if (backHandler.isRegistered(callback)) backHandler.unregister(callback)
    }
}
