package com.markettwits.profile.presentation.component.authorized.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.profile.presentation.component.authorized.domain.UserProfile
import com.markettwits.profile.presentation.component.authorized.presentation.store.AuthorizedProfileStore.Intent
import com.markettwits.profile.presentation.component.authorized.presentation.store.AuthorizedProfileStore.Label
import com.markettwits.profile.presentation.component.authorized.presentation.store.AuthorizedProfileStore.State

interface AuthorizedProfileStore : Store<Intent, State, Label> {
    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val message: String = "",
        val user: UserProfile? = null
    )

    sealed interface Intent {
        data object Retry : Intent
    }

    sealed interface Message {
        data object Loading : Message
        data class LoadingFailed(val message: String) : Message
        data class LoadingSuccess(val user: UserProfile) : Message
    }

    sealed interface Label {
        data object GoEditProfile : Label
    }


}
