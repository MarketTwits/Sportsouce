package com.markettwits.profile.presentation.component.edit_profile.presentation

import com.arkivanov.decompose.value.Value
import com.markettwits.cloud.model.auth.sign_in.response.User


interface EditProfile {
    val user : Value<User>
    val page : Value<List<EditProfileUiPage>>
    fun pop()
    fun saveChanges()
    fun obtainTextFiled(value : EditProfileUiPage)
}