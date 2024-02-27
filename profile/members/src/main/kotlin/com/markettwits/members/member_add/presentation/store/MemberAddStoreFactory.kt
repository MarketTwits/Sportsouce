package com.markettwits.members.member_add.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.members.member_add.presentation.store.MemberAddStore.Intent
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.markettwits.members.member_add.presentation.store.MemberAddStore.Label
import com.markettwits.members.member_add.presentation.store.MemberAddStore.State
import com.markettwits.members.member_add.presentation.store.MemberAddStore.Message

class MemberAddStoreFactory(private val storeFactory: StoreFactory) {

    fun create(): MemberAddStore {
        return MemberAddStoreImpl()
    }

    private inner class MemberAddStoreImpl :
        MemberAddStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "MemberAddStore",
            initialState = MemberAddStore.State,
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { MemberAddExecutor() },
            reducer = MemberAddReducer
        )
}