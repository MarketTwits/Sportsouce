package com.markettwits.sportsouce.profile.members.members_list.presentation.store.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.sportsouce.profile.members.members_list.domain.MembersListUseCase
import com.markettwits.sportsouce.profile.members.members_list.presentation.store.store.MembersListStore.Intent
import com.markettwits.sportsouce.profile.members.members_list.presentation.store.store.MembersListStore.Label
import com.markettwits.sportsouce.profile.members.members_list.presentation.store.store.MembersListStore.State

class MembersListStoreFactory(
    private val storeFactory: StoreFactory,
    private val useCase: MembersListUseCase
) {
    fun create(): MembersListStore = MembersListStoreImpl(useCase)

    private inner class MembersListStoreImpl(private val useCase: MembersListUseCase) :
        MembersListStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "MembersListStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { MembersListExecutor(useCase) },
            reducer = MembersListReducer
        )
}