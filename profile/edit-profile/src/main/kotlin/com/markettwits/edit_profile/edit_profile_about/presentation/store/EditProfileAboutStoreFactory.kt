package com.markettwits.edit_profile.edit_profile_about.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.edit_profile.edit_profile_about.presentation.store.EditProfileAboutStore.Intent
import com.markettwits.edit_profile.edit_profile_about.presentation.store.EditProfileAboutStore.Label
import com.markettwits.edit_profile.edit_profile_about.presentation.store.EditProfileAboutStore.State

class EditProfileAboutStoreFactory(private val storeFactory: StoreFactory) {

    fun create(): EditProfileAboutStore {
        return EditProfileAboutStoreImpl()
    }

    private inner class EditProfileAboutStoreImpl :
        EditProfileAboutStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "EditProfileAboutStore",
            initialState = State,
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { EditProfileAboutExecutor() },
            reducer = EditProfileAboutReducer
        )
}