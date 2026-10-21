package com.markettwits.sportsouce.starts.albums.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.sportsouce.starts.albums.domain.StartsAlbumsRepository

internal class StartsAlbumsStoreFactory(private val storeFactory: StoreFactory, private val repository: StartsAlbumsRepository) {
    fun create(): StartsAlbumsStore = object : StartsAlbumsStore,
        Store<StartsAlbumsStore.Intent, StartsAlbumsStore.State, StartsAlbumsStore.Label> by storeFactory.create(
            name = "StartsAlbumsStore", initialState = StartsAlbumsStore.State(),
            bootstrapper = SimpleBootstrapper(Unit), executorFactory = { StartsAlbumsExecutor(repository) }, reducer = StartsAlbumsReducer
        ) {}
}
