package com.markettwits.sportsouce.edit_profile.image.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.Dialog
import com.attafitamim.krop.core.crop.CropResult
import com.attafitamim.krop.core.crop.crop
import com.attafitamim.krop.core.crop.rememberImageCropper
import com.markettwits.sportsouce.edit_profile.image.presentation.component.EditProfileImageComponent
import com.markettwits.sportsouce.edit_profile.image.presentation.components.*
import com.markettwits.sportsouce.edit_profile.image.presentation.store.EditProfileImageStore
import kotlinx.coroutines.launch

@Composable
actual fun NewEditProfileImageScreen(component: EditProfileImageComponent) {
    Dialog(onDismissRequest = {
        component.obtainEvent(EditProfileImageStore.Intent.Dismiss)
    }) {
        val state by component.state.collectAsState()
        val scope = rememberCoroutineScope()
        val imageCropper = rememberImageCropper()
        if (imageCropper.cropState != null) {
            CropperDialog(cropState = imageCropper.cropState!!)
        }
        EditProfileImageScreenContent(
            state = state,
            dismiss = { component.obtainEvent(EditProfileImageStore.Intent.Dismiss) },
            onClickImageBox = {
                openImageFileDialog()?.let { file ->
                    val metadata = file.selectedImageMetadata()
                    val lastModified = file.lastModified()
                    scope.launch {
                        val result = imageCropper.crop(file = file)
                        if (result is CropResult.Success) {
                            component.updateCroppedImage(
                                bitmap = result.bitmap,
                                lastModified = lastModified,
                                sourceFileName = metadata.fileName,
                                sourceContentType = metadata.contentType
                            )
                        }
                    }
                }
            }
        )
    }
}
