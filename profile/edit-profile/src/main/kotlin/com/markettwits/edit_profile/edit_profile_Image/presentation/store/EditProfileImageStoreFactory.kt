package com.markettwits.edit_profile.edit_profile_Image.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.edit_profile.edit_profile_Image.data.EditProfileImageRepository
import com.markettwits.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore.Intent
import com.markettwits.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore.Label
import com.markettwits.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore.State

class EditProfileImageStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: EditProfileImageRepository
) {

    fun create(): EditProfileImageStore = EditProfileImageStoreImpl(repository)

    private inner class EditProfileImageStoreImpl(
        private val repository: EditProfileImageRepository
    ) :
        EditProfileImageStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "EditProfileImageStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { EditProfileImageExecutor(repository) },
            reducer = EditProfileImageReducer
        )
}