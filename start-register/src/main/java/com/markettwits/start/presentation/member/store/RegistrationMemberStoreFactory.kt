package com.markettwits.start.presentation.member.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.start.domain.StartStatement
import com.markettwits.start.presentation.member.domain.RegistrationMemberValidator
import com.markettwits.start.presentation.member.store.RegistrationMemberStore.Intent
import com.markettwits.start.presentation.member.store.RegistrationMemberStore.Label
import com.markettwits.start.presentation.member.store.RegistrationMemberStore.State

class RegistrationMemberStoreFactory(
    private val storeFactory: StoreFactory,
    private val startValidator: RegistrationMemberValidator
) {

    fun create(startStatement: StartStatement): RegistrationMemberStore =
        RegistrationMemberStoreImpl(startStatement, startValidator)

    private inner class RegistrationMemberStoreImpl(
        private val startStatement: StartStatement,
        private val validator: RegistrationMemberValidator
    ) :
        RegistrationMemberStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "RegistrationMemberStore",
            initialState = State(startStatement),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { RegistrationMemberExecutor(validator) },
            reducer = RegistrationMemberReducer
        )
}