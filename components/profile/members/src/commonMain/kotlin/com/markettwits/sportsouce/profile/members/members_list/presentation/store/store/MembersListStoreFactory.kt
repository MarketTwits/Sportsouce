package com.markettwits.sportsouce.profile.members.members_list.presentation.store.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.sportsouce.profile.members.members_list.domain.MembersListUseCase
import com.markettwits.sportsouce.profile.members.members_list.presentation.store.store.MembersListStore.*

class MembersListStoreFactory(
    private val storeFactory: StoreFactory,
    private val useCase: MembersListUseCase,
    private val exceptionTracker: ExceptionTracker,
) {
    fun create(): MembersListStore = MembersListStoreImpl(useCase)

    private inner class MembersListStoreImpl(private val useCase: MembersListUseCase) :
        MembersListStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "MembersListStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { MembersListExecutor(useCase, exceptionTracker) },
            reducer = MembersListReducer
        )
}