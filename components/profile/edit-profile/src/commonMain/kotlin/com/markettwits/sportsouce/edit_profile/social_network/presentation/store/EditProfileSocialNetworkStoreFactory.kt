package com.markettwits.sportsouce.edit_profile.social_network.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.sportsouce.edit_profile.social_network.domain.interactor.ProfileSocialNetworkInteractor
import com.markettwits.sportsouce.edit_profile.social_network.presentation.store.EditProfileSocialNetworkStore.*

class EditProfileSocialNetworkStoreFactory(
    private val storeFactory: StoreFactory,
    private val interactor: ProfileSocialNetworkInteractor
) {

    fun create(): EditProfileSocialNetworkStore = EditProfileSocialNetworkStoreImpl(interactor)

    private inner class EditProfileSocialNetworkStoreImpl(private val interactor: ProfileSocialNetworkInteractor) :
        EditProfileSocialNetworkStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "EditProfileSocialNetworkStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { EditProfileSocialNetworkExecutor(interactor) },
            reducer = EditProfileSocialNetworkReducer
        )
}
