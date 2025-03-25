package com.markettwits.sportsouce.club.info.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.sportsouce.club.common.domain.ClubRepository
import com.markettwits.sportsouce.club.info.domain.models.ClubInfo
import com.markettwits.sportsouce.club.info.presentation.store.ClubInfoStore.Intent
import com.markettwits.sportsouce.club.info.presentation.store.ClubInfoStore.Label
import com.markettwits.sportsouce.club.info.presentation.store.ClubInfoStore.State

internal class ClubInfoStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: ClubRepository
) {

    fun create(index: Int, items: List<ClubInfo>): ClubInfoStore = ClubInfoStoreImpl(
        index,
        items,
        repository
    )

    private inner class ClubInfoStoreImpl(
        private val index: Int,
        private val items: List<ClubInfo>,
        private val repository: ClubRepository
    ) : ClubInfoStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "ClubInfoStore",
            initialState = State(currentIndex = index, info = items),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { ClubInfoExecutor(repository) },
            reducer = ClubInfoReducer
        )
}