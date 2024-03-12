package com.markettwits.start.presentation.album.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.start.presentation.album.presentation.store.StartAlbumStore
import com.markettwits.start.presentation.album.presentation.store.StartAlbumStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StartAlbumComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: StartAlbumStoreFactory,
    private val images: List<String>,
    private val pop: () -> Unit
) : StartAlbumComponent, ComponentContext by componentContext {
    private val scope = CoroutineScope(Dispatchers.Main.immediate)
    private val store = instanceKeeper.getStore { storeFactory.create(images) }
    override val state: StateFlow<StartAlbumStore.State>
        get() = store.stateFlow

    override fun obtainEvent(intent: StartAlbumStore.Intent) {
        store.accept(intent)
    }

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    StartAlbumStore.Label.GoBack -> pop()
                }
            }
        }
    }
}