package com.markettwits.members.member_edit.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.members.member_common.domain.ProfileMember
import com.markettwits.members.member_edit.domain.MemberAddUseCase
import com.markettwits.members.member_edit.domain.MemberEditUseCase
import com.markettwits.members.member_edit.presentation.component.MemberEditComponent
import com.markettwits.members.member_edit.presentation.store.MemberEditStore.Intent
import com.markettwits.members.member_edit.presentation.store.MemberEditStore.Label
import com.markettwits.members.member_edit.presentation.store.MemberEditStore.State

class MemberEditStoreFactory(
    private val storeFactory: StoreFactory,
    private val useCase: MemberEditUseCase,
    private val addUseCase: MemberAddUseCase,
) {

    fun create(member: ProfileMember, mode: MemberEditComponent.Mode): MemberEditStore =
        MemberEditStoreImpl(member, useCase, addUseCase, mode)


    private inner class MemberEditStoreImpl(
        private val member: ProfileMember,
        private val editUseCase: MemberEditUseCase,
        private val addUseCase: MemberAddUseCase,
        private val mode: MemberEditComponent.Mode
    ) :
        MemberEditStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "MemberEditStore",
            initialState = State(member = member, mode = mode),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { MemberEditExecutor(editUseCase, addUseCase) },
            reducer = MemberEditReducer
        )
}