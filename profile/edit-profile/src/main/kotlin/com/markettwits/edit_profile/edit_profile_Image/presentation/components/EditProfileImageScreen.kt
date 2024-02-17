package com.markettwits.edit_profile.edit_profile_Image.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import com.markettwits.edit_profile.edit_profile_Image.presentation.component.EditProfileImageComponent
import com.markettwits.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore

@Composable
fun EditProfileImageScreen(component: EditProfileImageComponent) {
    Dialog(onDismissRequest = {
        component.obtainEvent(EditProfileImageStore.Intent.Dismiss)
    }) {
        EditProfileImageScreenContent(dismiss = {
            component.obtainEvent(EditProfileImageStore.Intent.Dismiss)
        }, onClickImageBox = {})
    }
}