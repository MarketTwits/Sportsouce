package com.markettwits.profile.presentation.component.edit_profile.presentation

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
