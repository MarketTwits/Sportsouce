package com.markettwits.sportsouce.edit_profile.image.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.sportsouce.edit_profile.image.data.EditProfileImageRepository
import com.markettwits.sportsouce.edit_profile.image.presentation.store.EditProfileImageStore.*

class EditProfileImageStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: EditProfileImageRepository
) {

    fun create(): EditProfileImageStore = EditProfileImageStoreImpl(repository)

    private inner class EditProfileImageStoreImpl(
        private val repository: EditProfileImageRepository,
    ) : EditProfileImageStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "EditProfileImageStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { EditProfileImageExecutor(repository) },
            reducer = EditProfileImageReducer
        )
}