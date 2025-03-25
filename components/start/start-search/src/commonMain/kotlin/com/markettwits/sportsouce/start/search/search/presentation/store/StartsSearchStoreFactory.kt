package com.markettwits.sportsouce.start.search.search.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.sportsouce.start.search.search.data.repository.StartsSearchRepository
import com.markettwits.sportsouce.start.search.search.presentation.store.StartsSearchStore.Intent
import com.markettwits.sportsouce.start.search.search.presentation.store.StartsSearchStore.Label
import com.markettwits.sportsouce.start.search.search.presentation.store.StartsSearchStore.State

class StartsSearchStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: StartsSearchRepository
) {
    fun create(): StartsSearchStore = StartsSearchStoreImpl(repository)

    private inner class StartsSearchStoreImpl(private val repository: StartsSearchRepository) :
        StartsSearchStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "StartsSearchStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { StartsSearchExecutor(repository) },
            reducer = StartsSearchReducer
        )
}