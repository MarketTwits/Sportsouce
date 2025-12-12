package com.markettwits.sportsouce.starts.favorites.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.sportsouce.starts.favorites.domain.StartFavoritesRepository
import com.markettwits.sportsouce.starts.favorites.presentation.store.StartsPopularStore.*

internal class StartsFavoritesStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: StartFavoritesRepository,
) {

    fun create(): StartsPopularStore = StartsPopularStoreImpl()

    private inner class StartsPopularStoreImpl :
        StartsPopularStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "StartsPopularStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { StartsPopularExecutor(repository) },
            reducer = StartsPopularReducer
        )
}