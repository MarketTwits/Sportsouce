package com.markettwits.sportsouce.edit_profile.sizes.presentation.store

import com.arkivanov.mvikotlin.core.store.*
import com.markettwits.sportsouce.edit_profile.sizes.domain.ProfileSizesRepository
import com.markettwits.sportsouce.edit_profile.sizes.presentation.store.ProfileSizesStore.*

class ProfileSizesStoreFactory(private val storeFactory: StoreFactory, private val repository: ProfileSizesRepository) {
    fun create(): ProfileSizesStore = object : ProfileSizesStore, Store<Intent, State, Label> by storeFactory.create(
        name = "ProfileSizesStore", initialState = State(), bootstrapper = SimpleBootstrapper(Unit),
        executorFactory = { ProfileSizesExecutor(repository) }, reducer = ProfileSizesReducer,
    ) {}
}
