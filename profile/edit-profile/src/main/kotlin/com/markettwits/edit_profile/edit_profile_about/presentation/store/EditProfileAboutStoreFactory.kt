package com.markettwits.edit_profile.edit_profile_about.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.edit_profile.edit_profile_about.data.EditProfileAboutRepository
import com.markettwits.edit_profile.edit_profile_about.presentation.store.EditProfileAboutStore.Intent
import com.markettwits.edit_profile.edit_profile_about.presentation.store.EditProfileAboutStore.Label
import com.markettwits.edit_profile.edit_profile_about.presentation.store.EditProfileAboutStore.State

class EditProfileAboutStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: EditProfileAboutRepository
) {
    fun create(): EditProfileAboutStore = EditProfileAboutStoreImpl(repository)

    private inner class EditProfileAboutStoreImpl(repository: EditProfileAboutRepository) :
        EditProfileAboutStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "EditProfileAboutStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { EditProfileAboutExecutor(repository) },
            reducer = EditProfileAboutReducer
        )
}