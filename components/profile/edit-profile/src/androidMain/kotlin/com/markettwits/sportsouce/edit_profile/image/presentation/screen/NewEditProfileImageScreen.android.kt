package com.markettwits.sportsouce.edit_profile.image.presentation.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import com.attafitamim.krop.core.crop.CropResult
import com.attafitamim.krop.core.crop.crop
import com.attafitamim.krop.core.crop.rememberImageCropper
import com.markettwits.sportsouce.edit_profile.image.presentation.component.EditProfileImageComponent
import com.markettwits.sportsouce.edit_profile.image.presentation.components.CropperDialog
import com.markettwits.sportsouce.edit_profile.image.presentation.components.EditProfileImageScreenContent
import com.markettwits.sportsouce.edit_profile.image.presentation.components.selectedImageMetadata
import com.markettwits.sportsouce.edit_profile.image.presentation.components.updateCroppedImage
import com.markettwits.sportsouce.edit_profile.image.presentation.store.EditProfileImageStore
import kotlinx.coroutines.launch

@Composable
actual fun NewEditProfileImageScreen(component: EditProfileImageComponent) {
    Dialog(onDismissRequest = {
        component.obtainEvent(EditProfileImageStore.Intent.Dismiss)
    }) {
        val state by component.state.collectAsState()
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        val imageCropper = rememberImageCropper()
        val contract = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            scope.launch {
                val metadata = uri?.let { context.selectedImageMetadata(it) }
                val result = uri?.let { imageCropper.crop(it, context) }
                if (result is CropResult.Success) {
                    component.updateCroppedImage(
                        bitmap = result.bitmap,
                        lastModified = System.currentTimeMillis(),
                        sourceFileName = metadata?.fileName,
                        sourceContentType = metadata?.contentType
                    )
                }
            }
        }
        if (imageCropper.cropState != null) {
            CropperDialog(cropState = imageCropper.cropState!!)
        }
        EditProfileImageScreenContent(
            state = state,
            dismiss = { component.obtainEvent(EditProfileImageStore.Intent.Dismiss) },
            onClickImageBox = {
                contract.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        )
    }
}
