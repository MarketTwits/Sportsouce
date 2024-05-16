package com.markettwits.club.registration.presentation.store.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.IntentAction
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
    fun create(workoutId: Int): WorkoutRegistrationStore =
        WorkoutRegistrationStoreImpl(useCase, intentAction, workoutId)

    private inner class WorkoutRegistrationStoreImpl(
        private val useCase: WorkoutRegistrationUseCase,
        private val intentAction: IntentAction,
        private val workoutId: Int
    ) :
        WorkoutRegistrationStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "WorkoutRegistrationStore",
            initialState = State(
                form = WorkoutRegistrationForm(workoutId, "", "", "")
            ),
            executorFactory = { WorkoutRegistrationExecutor(useCase, intentAction) },
            reducer = WorkoutRegistrationReducer
        )
}