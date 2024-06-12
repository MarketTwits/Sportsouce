package com.markettwits.club.registration.presentation.store.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.IntentAction
import com.markettwits.club.registration.domain.RegistrationType
import com.markettwits.club.registration.domain.WorkoutRegistrationForm
import com.markettwits.club.registration.domain.WorkoutRegistrationUseCase
import com.markettwits.club.registration.presentation.store.store.WorkoutRegistrationStore.Intent
import com.markettwits.club.registration.presentation.store.store.WorkoutRegistrationStore.Label
import com.markettwits.club.registration.presentation.store.store.WorkoutRegistrationStore.State

class WorkoutRegistrationStoreFactory(
    private val storeFactory: StoreFactory,
    private val useCase: WorkoutRegistrationUseCase,
    private val intentAction: IntentAction
) {
    fun create(registrationType: RegistrationType): WorkoutRegistrationStore =
        WorkoutRegistrationStoreImpl(useCase, intentAction, registrationType)

    private inner class WorkoutRegistrationStoreImpl(
        private val useCase: WorkoutRegistrationUseCase,
        private val intentAction: IntentAction,
        private val registrationType: RegistrationType
    ) :
        WorkoutRegistrationStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "WorkoutRegistrationStore",
            initialState = State(
                form = WorkoutRegistrationForm(registrationType, "", "", "")
            ),
            executorFactory = { WorkoutRegistrationExecutor(useCase, intentAction) },
            reducer = WorkoutRegistrationReducer
        )
}