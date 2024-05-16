package com.markettwits.club.info.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.club.info.presentation.store.ClubInfoStore.Intent
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.markettwits.club.info.domain.ClubInfoRepository
import com.markettwits.club.info.presentation.store.ClubInfoStore.Label
import com.markettwits.club.info.presentation.store.ClubInfoStore.State
import com.markettwits.club.info.presentation.store.ClubInfoStore.Message

internal class ClubInfoStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: ClubInfoRepository
) {

    fun create(index: Int): ClubInfoStore = ClubInfoStoreImpl(
        index,
        repository
    )

    private inner class ClubInfoStoreImpl(
        private val index: Int,
        private val repository: ClubInfoRepository
    ) : ClubInfoStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "ClubInfoStore",
            initialState = ClubInfoStore.State(currentIndex = index),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { ClubInfoExecutor(repository) },
            reducer = ClubInfoReducer
        )
}