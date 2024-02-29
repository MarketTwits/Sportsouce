package com.markettwits.members.member_detail.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.members.member_common.domain.ProfileMember
import com.markettwits.members.member_detail.domain.MemberDetailUseCase
import com.markettwits.members.member_detail.presentation.store.MemberDetailStore.Intent
import com.markettwits.members.member_detail.presentation.store.MemberDetailStore.Label
import com.markettwits.members.member_detail.presentation.store.MemberDetailStore.State

class MemberDetailStoreFactory(
    private val storeFactory: StoreFactory,
    private val useCase: MemberDetailUseCase
) {

    fun create(member: ProfileMember): MemberDetailStore = MemberDetailStoreImpl(member, useCase)


    private inner class MemberDetailStoreImpl(
        private val member: ProfileMember,
        private val useCase: MemberDetailUseCase
    ) :
        MemberDetailStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "MemberDetailStore",
            initialState = State(member = member),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { MemberDetailExecutor(useCase) },
            reducer = MemberDetailReducer
        )
}