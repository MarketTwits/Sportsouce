package com.markettwits.sportsouce.edit_profile.edit_profile_about.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.Dialog
import com.markettwits.sportsouce.edit_profile.edit_profile_about.presentation.component.EditProfileAboutComponent
import com.markettwits.sportsouce.edit_profile.edit_profile_about.presentation.store.EditProfileAboutStore

@Composable
fun EditProfileAboutScreen(component: EditProfileAboutComponent) {
    Dialog(onDismissRequest = {
        component.obtainEvent(EditProfileAboutStore.Intent.Dismiss)
    }) {
        val state by component.state.collectAsState()

        EditProfileAboutContent(
            state = state,
            apply = {
                component.obtainEvent(EditProfileAboutStore.Intent.Apply)
            },
            dismiss = {
                component.obtainEvent(EditProfileAboutStore.Intent.Dismiss)
            }, onValueChanged = {
                component.obtainEvent(EditProfileAboutStore.Intent.OnValueChanged(it))
            })
    }
}