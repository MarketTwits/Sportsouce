package com.markettwits.sportsouce.edit_profile.edit_profile_info.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.sportsouce.edit_profile.edit_profile_info.domain.EditProfileInfoRepository
import com.markettwits.sportsouce.edit_profile.edit_profile_info.presentation.store.EditProfileInfoStore.Intent
import com.markettwits.sportsouce.edit_profile.edit_profile_info.presentation.store.EditProfileInfoStore.Label
import com.markettwits.sportsouce.edit_profile.edit_profile_info.presentation.store.EditProfileInfoStore.State

class EditProfileInfoStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: EditProfileInfoRepository
) {

    fun create(): EditProfileInfoStore {
        return EditProfileInfoStoreImpl(repository)
    }

    private inner class EditProfileInfoStoreImpl(repository: EditProfileInfoRepository) :
        EditProfileInfoStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "ProfileInfoStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { EditProfileInfoExecutor(repository) },
            reducer = EditProfileInfoReducer
        )
}