package com.markettwits.edit_profile.edit_menu.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.edit_profile.edit_menu.presentation.store.EditProfileMenuStore.Intent
import com.markettwits.edit_profile.edit_menu.presentation.store.EditProfileMenuStore.Label
import com.markettwits.edit_profile.edit_menu.presentation.store.EditProfileMenuStore.State

class EditProfileMenuStoreFactory(private val storeFactory: StoreFactory) {

    fun create(): EditProfileMenuStore = EditProfileMenuStoreImpl()


    private inner class EditProfileMenuStoreImpl :
        EditProfileMenuStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "EditProfileMenuStore",
            initialState = State,
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { EditProfileMenuExecutor() },
            reducer = EditProfileMenuReducer
        )
}