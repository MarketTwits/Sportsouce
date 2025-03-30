package com.markettwits.sportsouce.edit_profile.edit_profile_sign_out.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.sportsouce.edit_profile.edit_profile_sign_out.domain.SignOutUseCase
import com.markettwits.sportsouce.edit_profile.edit_profile_sign_out.presentation.store.EditProfileSignOutStore.Intent
import com.markettwits.sportsouce.edit_profile.edit_profile_sign_out.presentation.store.EditProfileSignOutStore.Label
import com.markettwits.sportsouce.edit_profile.edit_profile_sign_out.presentation.store.EditProfileSignOutStore.State

class EditProfileSignOutStoreFactory(
    private val storeFactory: StoreFactory,
    private val useCase: SignOutUseCase
) {

    fun create(): EditProfileSignOutStore = EditProfileSignOutStoreImpl(useCase)

    private inner class EditProfileSignOutStoreImpl(
        private val useCase: SignOutUseCase
    ) :
        EditProfileSignOutStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "EditProfileSignOutStore",
            initialState = State,
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { EditProfileSignOutExecutor(useCase) },
            reducer = EditProfileSignOutReducer
        )
}