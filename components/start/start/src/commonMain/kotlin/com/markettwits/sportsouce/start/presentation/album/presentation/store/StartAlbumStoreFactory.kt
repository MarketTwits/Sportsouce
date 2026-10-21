package com.markettwits.sportsouce.start.presentation.album.presentation.store

import com.markettwits.sportsouce.start.presentation.album.domain.StartAlbumRepository
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.sportsouce.start.presentation.album.presentation.store.StartAlbumStore.Intent
import com.markettwits.sportsouce.start.presentation.album.presentation.store.StartAlbumStore.Label
import com.markettwits.sportsouce.start.presentation.album.presentation.store.StartAlbumStore.State

class StartAlbumStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: StartAlbumRepository,
) {

    fun create(images: List<String>, albumId: Int? = null): StartAlbumStore =
        StartAlbumStoreImpl(images, albumId)

    private inner class StartAlbumStoreImpl(
        private val images: List<String>,
        private val albumId: Int?
    ) :
        StartAlbumStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "StartAlbumStore",
            initialState = State(images = images),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { StartAlbumExecutor(albumId, repository) },
            reducer = StartAlbumReducer
        )
}