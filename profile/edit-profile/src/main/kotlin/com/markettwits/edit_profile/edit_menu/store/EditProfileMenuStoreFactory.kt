package com.markettwits.edit_profile.edit_menu.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.edit_profile.edit_menu.store.EditProfileMenuStore.Intent
import com.markettwits.edit_profile.edit_menu.store.EditProfileMenuStore.Label
import com.markettwits.edit_profile.edit_menu.store.EditProfileMenuStore.State

class EditProfileMenuStoreFactory(private val storeFactory: StoreFactory) {

    fun create(): EditProfileMenuStore {
        return EditProfileMenuStoreImpl()
    }

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