package com.markettwits.sportsouce.start.presentation.result.store

import app.cash.paging.PagingData
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.sportsouce.start.domain.StartRepository
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult
import com.markettwits.sportsouce.start.presentation.result.store.StartMemberResultsStore.*
import kotlinx.coroutines.flow.flowOf

class StartMemberResultsStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: StartRepository,
    private val startId: Int,
) {

    fun create(initialMemberResult: List<MemberResult>): StartMemberResultsStore = object : StartMemberResultsStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "StartMemberResultsStore",
            initialState = State(membersItems = flowOf(PagingData.empty())),
            executorFactory = { StartMemberResultsExecutor(repository, startId, initialMemberResult) },
            reducer = StartMemberResultsReducer
        ) {}
}