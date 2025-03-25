package com.markettwits.sportsouce.edit_profile.edit_profile_Image.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.Dialog
import com.markettwits.sportsouce.edit_profile.edit_profile_Image.presentation.component.EditProfileImageComponent
import com.markettwits.sportsouce.edit_profile.edit_profile_Image.presentation.components.EditProfileImageScreenContent
import com.markettwits.sportsouce.edit_profile.edit_profile_Image.presentation.components.openImageFileDialog
import com.markettwits.sportsouce.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore

@Composable
actual fun NewEditProfileImageScreen(component: EditProfileImageComponent) {
    Dialog(onDismissRequest = {
        component.obtainEvent(EditProfileImageStore.Intent.Dismiss)
    }) {
        val state by component.state.collectAsState()
        EditProfileImageScreenContent(
            state = state,
            dismiss = { component.obtainEvent(EditProfileImageStore.Intent.Dismiss) },
            onClickImageBox = {
                openImageFileDialog()?.let { file ->
                    val byteArray = file.readBytes()
                    val lastModified = file.lastModified()
                    component.obtainEvent(
                        EditProfileImageStore.Intent.UpdateImage(
                            byteArray,
                            lastModified
                        )
                    )
                }
            })
    }
}