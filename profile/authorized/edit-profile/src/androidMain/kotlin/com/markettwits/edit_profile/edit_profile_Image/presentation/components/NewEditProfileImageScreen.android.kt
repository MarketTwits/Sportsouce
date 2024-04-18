package com.markettwits.edit_profile.edit_profile_Image.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.Dialog
import com.markettwits.edit_profile.edit_profile_Image.presentation.component.EditProfileImageComponent
import com.markettwits.edit_profile.edit_profile_Image.presentation.components.content.EditProfileImageScreenContent
import com.markettwits.edit_profile.edit_profile_Image.presentation.components.image_picker.utils.byteArrayToFile
import com.markettwits.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher

@Composable
actual fun NewEditProfileImageScreen(component: EditProfileImageComponent) {
    val state by component.state.collectAsState()
    Dialog(onDismissRequest = {
        component.obtainEvent(EditProfileImageStore.Intent.Dismiss)
    }) {
        val multipleImagePicker = rememberImagePickerLauncher(
            selectionMode = SelectionMode.Single,
            scope = rememberCoroutineScope(),
            onResult = { byteArrays ->
                byteArrays.forEach {
                    byteArrayToFile(it, "selectedImage")?.let { file ->
                        component.obtainEvent(EditProfileImageStore.Intent.UpdateImage(file))
                    }
                }
            }
        )
        EditProfileImageScreenContent(
            state = state,
            dismiss = { component.obtainEvent(EditProfileImageStore.Intent.Dismiss) },
            onClickImageBox = {
                multipleImagePicker.launch()
            })
    }
}