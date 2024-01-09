package com.markettwits.profile.presentation.component.edit_profile.presentation

import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.profile.presentation.component.edit_profile.models.CityUi
import com.markettwits.profile.presentation.component.edit_profile.models.TeamUi
import kotlinx.serialization.Serializable

@Serializable
sealed interface EditProfileUiState {
    val data: List<EditProfileUiPage>
    val isLoading: Boolean
    val message: String
    @Serializable
    data class Base(
        override val data: List<EditProfileUiPage>,
        override val isLoading: Boolean = false,
        override val message: String = ""
    ) : EditProfileUiState
    @Serializable
    data class Loading(
        override val data: List<EditProfileUiPage> = emptyList(),
        override val isLoading: Boolean = true,
        override val message: String = ""
    ) : EditProfileUiState
    @Serializable
    data class Error(
        override val data: List<EditProfileUiPage> = emptyList(),
        override val isLoading: Boolean = false,
        override val message: String
    ) : EditProfileUiState
    @Serializable
    data class LoadingChanges(
        override val data: List<EditProfileUiPage> = emptyList(),
        override val isLoading: Boolean = false,
        override val message: String = ""
    ) : EditProfileUiState
}
