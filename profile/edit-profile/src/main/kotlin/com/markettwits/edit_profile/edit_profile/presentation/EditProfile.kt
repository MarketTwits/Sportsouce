package com.markettwits.edit_profile.edit_profile.presentation

import com.arkivanov.decompose.value.Value
import com.markettwits.profile.presentation.component.edit_profile.presentation.EditProfileEvent
import com.markettwits.profile.presentation.component.edit_profile.presentation.EditProfileUiPage
import com.markettwits.profile.presentation.component.edit_profile.presentation.EditProfileUiState
import kotlinx.coroutines.flow.SharedFlow


interface EditProfileComponent {
    val state : Value<EditProfileUiState>
    val events: SharedFlow<EditProfileEvent>
    fun launch()
    fun pop()
    fun saveChanges()
    fun messageHasBeenShowed()
    fun obtainTextFiled(value : EditProfileUiPage)
}