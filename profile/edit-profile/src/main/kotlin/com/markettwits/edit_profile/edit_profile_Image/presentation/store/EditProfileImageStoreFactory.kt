package com.markettwits.edit_profile.edit_profile_Image.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore.Intent
import com.markettwits.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore.Label
import com.markettwits.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore.State

class EditProfileImageStoreFactory(private val storeFactory: StoreFactory) {

    fun create(): EditProfileImageStore {
        return EditProfileImageStoreImpl()
    }

    private inner class EditProfileImageStoreImpl :
        EditProfileImageStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "EditProfileImageStore",
            initialState = State,
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { EditProfileImageExecutor() },
            reducer = EditProfileImageReducer
        )
}