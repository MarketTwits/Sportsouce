package com.markettwits.sportsouce.club.registration.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.IntentAction
import com.markettwits.sportsouce.club.registration.domain.RegistrationType
import com.markettwits.sportsouce.club.registration.domain.WorkoutRegistrationForm
import com.markettwits.sportsouce.club.registration.domain.WorkoutRegistrationUseCase

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
    ) : WorkoutRegistrationStore,
        Store<WorkoutRegistrationStore.Intent, WorkoutRegistrationStore.State, WorkoutRegistrationStore.Label> by storeFactory.create(
            name = "WorkoutRegistrationStore",
            initialState = WorkoutRegistrationStore.State(form = WorkoutRegistrationForm.EMPTY),
            executorFactory = { WorkoutRegistrationExecutor(useCase, intentAction,registrationType) },
            reducer = WorkoutRegistrationReducer,
            bootstrapper = SimpleBootstrapper(Unit)
        )
}