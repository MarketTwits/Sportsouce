package com.markettwits.start.presentation.result.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.start.presentation.result.store.StartMemberResultsStore.Intent
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.markettwits.start.domain.StartRepository
import com.markettwits.start.presentation.result.model.MemberResult
import com.markettwits.start.presentation.result.store.StartMemberResultsStore.Label
import com.markettwits.start.presentation.result.store.StartMemberResultsStore.State
import com.markettwits.start.presentation.result.store.StartMemberResultsStore.Message

class StartMemberResultsStoreFactory(
    private val storeFactory: StoreFactory,
) {

    fun create(membersResult: List<MemberResult>): StartMemberResultsStore {
        return StartMemberResultsStoreImpl(membersResult)
    }

    private inner class StartMemberResultsStoreImpl(
        private val memberResult: List<MemberResult>
    ) :
        StartMemberResultsStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "StartMemberResultsStore",
            initialState = State(memberResult, memberResult),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { StartMemberResultsExecutor() },
            reducer = StartMemberResultsReducer
        )
}