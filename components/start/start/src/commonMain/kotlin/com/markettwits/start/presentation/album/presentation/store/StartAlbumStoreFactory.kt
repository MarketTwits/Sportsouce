package com.markettwits.start.presentation.album.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.start.presentation.album.presentation.store.StartAlbumStore.Intent
import com.markettwits.start.presentation.album.presentation.store.StartAlbumStore.Label
import com.markettwits.start.presentation.album.presentation.store.StartAlbumStore.State

class StartAlbumStoreFactory(private val storeFactory: StoreFactory) {

    fun create(images: List<String>): StartAlbumStore =
        StartAlbumStoreImpl(images)

    private inner class StartAlbumStoreImpl(
        private val images: List<String>
    ) :
        StartAlbumStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "StartAlbumStore",
            initialState = State(images = images),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { StartAlbumExecutor() },
            reducer = StartAlbumReducer
        )
}