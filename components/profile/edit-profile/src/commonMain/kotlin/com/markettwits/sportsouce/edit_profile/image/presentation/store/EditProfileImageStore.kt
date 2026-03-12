package com.markettwits.sportsouce.edit_profile.image.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.sportsouce.edit_profile.image.presentation.store.EditProfileImageStore.*

interface EditProfileImageStore : Store<Intent, State, Label> {
    data class State(
        val isLoading: Boolean = false,
        val isFailed: Boolean = false,
        val isSuccess: Boolean = false,
        val message: String = ""
    )

    sealed interface Intent {
        data object Dismiss : Intent
        data class UpdateImage(
            val data: ByteArray,
            val lastModified: Long,
            val fileName: String,
            val contentType: String,
        ) : Intent
    }

    sealed interface Message {
        data object ShowLoading : Message
        data class ShowError(val message: String) : Message
        data object ShowSuccess : Message
    }


    sealed interface Label {
        data object Dismiss : Label
    }


}
