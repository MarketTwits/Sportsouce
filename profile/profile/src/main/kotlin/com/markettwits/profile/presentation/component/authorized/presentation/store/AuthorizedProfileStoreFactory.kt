package com.markettwits.profile.presentation.component.authorized.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.profile.presentation.component.authorized.data.AuthorizedProfileRepository
import com.markettwits.profile.presentation.component.authorized.presentation.store.AuthorizedProfileStore.Intent
import com.markettwits.profile.presentation.component.authorized.presentation.store.AuthorizedProfileStore.Label
import com.markettwits.profile.presentation.component.authorized.presentation.store.AuthorizedProfileStore.State

class AuthorizedProfileStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: AuthorizedProfileRepository
) {

    fun create(): AuthorizedProfileStore {
        return AuthorizedProfileStoreImpl(repository)
    }

    private inner class AuthorizedProfileStoreImpl(private val repository: AuthorizedProfileRepository) :
        AuthorizedProfileStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "AuthorizedProfileStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { AuthorizedProfileExecutor(repository) },
            reducer = AuthorizedProfileReducer
        )
}