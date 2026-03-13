package com.markettwits.sportsouce.profile.authorized.authorized.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.IntentAction
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.sportsouce.profile.authorized.authorized.domain.UserProfileInteractor
import com.markettwits.sportsouce.profile.authorized.authorized.presentation.store.AuthorizedProfileStore.Intent
import com.markettwits.sportsouce.profile.authorized.authorized.presentation.store.AuthorizedProfileStore.State
import com.markettwits.sportsouce.starts.recent.domain.StartRecentRepository

class AuthorizedProfileStoreFactory(
    private val storeFactory: StoreFactory,
    private val interactor: UserProfileInteractor,
    private val recentRepository: StartRecentRepository,
    private val exceptionTracker: ExceptionTracker,
    private val intentAction: IntentAction,
) {

    fun create(): AuthorizedProfileStore {
        return AuthorizedProfileStoreImpl(interactor, recentRepository, exceptionTracker, intentAction)
    }

    private inner class AuthorizedProfileStoreImpl(
        private val interactor: UserProfileInteractor,
        private val recentRepository: StartRecentRepository,
        private val exceptionTracker: ExceptionTracker,
        private val intentAction: IntentAction,
    ) : AuthorizedProfileStore, Store<Intent, State, Unit> by storeFactory.create(
            name = "AuthorizedProfileStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = {
                AuthorizedProfileExecutor(
                    interactor,
                    recentRepository,
                    exceptionTracker,
                    intentAction
                )
            },
            reducer = AuthorizedProfileReducer
        )
}
