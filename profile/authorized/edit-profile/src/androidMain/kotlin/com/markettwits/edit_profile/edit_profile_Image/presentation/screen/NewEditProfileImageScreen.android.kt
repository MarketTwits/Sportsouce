package com.markettwits.edit_profile.edit_profile_Image.presentation.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import com.markettwits.edit_profile.edit_profile_Image.presentation.component.EditProfileImageComponent
import com.markettwits.edit_profile.edit_profile_Image.presentation.components.CropperDialog
import com.markettwits.edit_profile.edit_profile_Image.presentation.components.EditProfileImageScreenContent
import com.markettwits.edit_profile.edit_profile_Image.presentation.components.convertBitmapToFile
import com.markettwits.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore
import com.mr0xf00.easycrop.CropResult
import com.mr0xf00.easycrop.crop
import com.mr0xf00.easycrop.rememberImageCropper
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
                val result = uri?.let { imageCropper.crop(it, context) }
                if (result is CropResult.Success) {
                    val cropFile = convertBitmapToFile(context, result.bitmap.asAndroidBitmap())
                  //  component.obtainEvent(EditProfileImageStore.Intent.UpdateImage(cropFile))
                }
            }
        }
        if (imageCropper.cropState != null)
            CropperDialog(cropState = imageCropper.cropState!!)
        EditProfileImageScreenContent(
            state = state,
            dismiss = { component.obtainEvent(EditProfileImageStore.Intent.Dismiss) },
            onClickImageBox = {
                contract.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            })
    }
}