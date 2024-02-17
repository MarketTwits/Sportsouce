package com.markettwits.edit_profile.social_network.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.edit_profile.social_network.domain.ProfileSocialNetworkRepository
import com.markettwits.edit_profile.social_network.domain.UserSocialNetwork
import com.markettwits.edit_profile.social_network.presentation.store.EditProfileSocialNetworkStore.Intent
import com.markettwits.edit_profile.social_network.presentation.store.EditProfileSocialNetworkStore.Label
import com.markettwits.edit_profile.social_network.presentation.store.EditProfileSocialNetworkStore.State

class EditProfileSocialNetworkStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: ProfileSocialNetworkRepository
) {

    fun create(): EditProfileSocialNetworkStore {
        return EditProfileSocialNetworkStoreImpl(repository)
    }

    private inner class EditProfileSocialNetworkStoreImpl(repository: ProfileSocialNetworkRepository) :
        EditProfileSocialNetworkStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "EditProfileSocialNetworkStore",
            initialState = State(
                data = UserSocialNetwork(
                    "",
                    "",
                    "",
                    ""
                )
            ),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { EditProfileSocialNetworkExecutor(repository) },
            reducer = EditProfileSocialNetworkReducer
        )
}