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
//        val state by component.state.collectAsState()
//        val scope = rememberCoroutineScope()
//        val context = LocalContext.current
//        val imageCropper = rememberImageCropper()
//        val contract = rememberLauncherForActivityResult(
//            contract = ActivityResultContracts.PickVisualMedia()
//        ) { uri ->
//            scope.launch {
//                val result = uri?.let { imageCropper.crop(it, context) }
//                if (result is CropResult.Success) {
//                    val cropFile = convertBitmapToFile(context, result.bitmap.asAndroidBitmap())
//                    component.obtainEvent(EditProfileImageStore.Intent.UpdateImage(cropFile))
//                }
//            }
//        }
//        if (imageCropper.cropState != null)
//            CropperDialog(cropState = imageCropper.cropState!!)
//        EditProfileImageScreenContent(
//            state = state,
//            dismiss = { component.obtainEvent(EditProfileImageStore.Intent.Dismiss) },
//            onClickImageBox = {
//                contract.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//            })
    }
}