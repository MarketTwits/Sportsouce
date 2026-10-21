package com.markettwits.sportsouce.starts.albums.filter.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.sportsouce.starts.albums.filter.store.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

internal class AlbumsFilterComponentBase(
    context: ComponentContext, year: Int?, storeFactory: AlbumsFilterStoreFactory,
    private val apply: (Int?) -> Unit, private val dismiss: () -> Unit,
) : AlbumsFilterComponent, ComponentContext by context {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val store = instanceKeeper.getStore { storeFactory.create(year) }
    @OptIn(ExperimentalCoroutinesApi::class)
    override val state = store.stateFlow
    override fun obtainEvent(intent: AlbumsFilterStore.Intent) = store.accept(intent)
    init {
        store.labels.onEach { when (it) {
            is AlbumsFilterStore.Label.Apply -> apply(it.year)
            AlbumsFilterStore.Label.Dismiss -> dismiss()
        } }.launchIn(scope)
        lifecycle.doOnDestroy { scope.cancel() }
    }
}
