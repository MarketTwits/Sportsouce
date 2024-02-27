package com.markettwits.members.member_add.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.members.member_add.presentation.store.MemberAddStore
import com.markettwits.members.member_add.presentation.store.MemberAddStoreFactory
import kotlinx.coroutines.flow.StateFlow

class MemberAddComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: MemberAddStoreFactory
) : MemberAddComponent,
    ComponentContext by componentContext {
    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }
    override val state: StateFlow<MemberAddStore.State> = store.stateFlow
    override fun obtainEvent(intent: MemberAddStore.Intent) {
        store.accept(intent)
    }
}
