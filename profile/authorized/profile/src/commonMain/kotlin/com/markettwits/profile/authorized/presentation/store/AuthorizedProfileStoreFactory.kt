package com.markettwits.profile.authorized.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.IntentAction
import com.markettwits.profile.authorized.domain.UserProfileInteractor
import com.markettwits.profile.authorized.presentation.store.AuthorizedProfileStore.Intent
import com.markettwits.profile.authorized.presentation.store.AuthorizedProfileStore.Label
import com.markettwits.profile.authorized.presentation.store.AuthorizedProfileStore.State

class AuthorizedProfileStoreFactory(
    private val storeFactory: StoreFactory,
    private val interactor: UserProfileInteractor,
    private val intentAction: IntentAction
) {

    fun create(): AuthorizedProfileStore {
        return AuthorizedProfileStoreImpl(interactor, intentAction)
    }

    private inner class AuthorizedProfileStoreImpl(
        private val interactor: UserProfileInteractor,
        private val intentAction: IntentAction
    ) :
        AuthorizedProfileStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "AuthorizedProfileStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { AuthorizedProfileExecutor(interactor, intentAction) },
            reducer = AuthorizedProfileReducer
        )
}