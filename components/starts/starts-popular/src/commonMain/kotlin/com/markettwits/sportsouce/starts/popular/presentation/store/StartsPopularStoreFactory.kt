package com.markettwits.sportsouce.starts.popular.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.sportsouce.starts.popular.domain.StartsPopularRepository
import com.markettwits.sportsouce.starts.popular.presentation.store.StartsPopularStore.Intent
import com.markettwits.sportsouce.starts.popular.presentation.store.StartsPopularStore.Label
import com.markettwits.sportsouce.starts.popular.presentation.store.StartsPopularStore.State

internal class StartsPopularStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: StartsPopularRepository
) {

    fun create(): StartsPopularStore = StartsPopularStoreImpl(repository)

    private inner class StartsPopularStoreImpl(
        private val repository: StartsPopularRepository
    ) :
        StartsPopularStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "StartsPopularStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { StartsPopularExecutor(repository) },
            reducer = StartsPopularReducer
        )
}