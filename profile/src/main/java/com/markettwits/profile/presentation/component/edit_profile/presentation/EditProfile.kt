package com.markettwits.profile.presentation.component.edit_profile.presentation

import com.arkivanov.decompose.value.Value
import com.markettwits.cloud.model.auth.sign_in.response.User
import kotlinx.coroutines.flow.SharedFlow


interface EditProfile {
   // val page : Value<List<EditProfileUiPage>>
    val state : Value<EditProfileUiState>
    val events: SharedFlow<EditProfileEvent>
    fun pop()
    fun saveChanges()
    fun messageHasBeenShowed()
    fun obtainTextFiled(value : EditProfileUiPage)
}