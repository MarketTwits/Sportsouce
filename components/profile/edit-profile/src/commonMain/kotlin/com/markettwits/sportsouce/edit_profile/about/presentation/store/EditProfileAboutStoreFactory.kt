package com.markettwits.sportsouce.edit_profile.about.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.sportsouce.edit_profile.about.data.EditProfileAboutRepository
import com.markettwits.sportsouce.edit_profile.about.presentation.store.EditProfileAboutStore.*

class EditProfileAboutStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: EditProfileAboutRepository
) {
    fun create(): EditProfileAboutStore = EditProfileAboutStoreImpl(repository)

    private inner class EditProfileAboutStoreImpl(
        repository: EditProfileAboutRepository,
    ) :
        EditProfileAboutStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "EditProfileAboutStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { EditProfileAboutExecutor(repository) },
            reducer = EditProfileAboutReducer
        )
}