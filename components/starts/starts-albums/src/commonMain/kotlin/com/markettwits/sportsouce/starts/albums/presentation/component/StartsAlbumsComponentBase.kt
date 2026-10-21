package com.markettwits.sportsouce.starts.albums.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.markettwits.sportsouce.starts.albums.presentation.store.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

internal class StartsAlbumsComponentBase(
    context: ComponentContext, storeFactory: StartsAlbumsStoreFactory,
    private val pop: () -> Unit, private val openAlbum: (Int) -> Unit, private val openFilters: (Int?) -> Unit,
) : StartsAlbumsComponent, ComponentContext by context {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val store = instanceKeeper.getStore { storeFactory.create() }
    @OptIn(ExperimentalCoroutinesApi::class)
    override val state = store.stateFlow
    override fun obtainEvent(intent: StartsAlbumsStore.Intent) = store.accept(intent)
    init {
        store.labels.onEach { when (it) {
            StartsAlbumsStore.Label.Back -> pop()
            is StartsAlbumsStore.Label.OpenFilters -> openFilters(it.year)
            is StartsAlbumsStore.Label.OpenAlbum -> openAlbum(it.id)
        } }.launchIn(scope)
        lifecycle.doOnDestroy { scope.cancel() }
    }
}
