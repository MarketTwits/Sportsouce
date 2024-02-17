package com.markettwits.edit_profile.edit_profile_about.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog
import com.markettwits.edit_profile.edit_profile_about.presentation.component.EditProfileAboutComponent
import com.markettwits.edit_profile.edit_profile_about.presentation.store.EditProfileAboutStore

@Composable
fun EditProfileAboutScreen(component: EditProfileAboutComponent) {
    Dialog(onDismissRequest = {
        component.obtainEvent(EditProfileAboutStore.Intent.Dismiss)
    }) {
        var text by remember {
            mutableStateOf("")
        }
        var isError by remember {
            mutableStateOf(false)
        }

        EditProfileAboutContent(
            value = text,
            apply = {

            },
            error = isError,
            dismiss = {
                component.obtainEvent(EditProfileAboutStore.Intent.Dismiss)
            }, onValueChanged = {
                text = it
                isError = text.length > 120
            })
    }
}