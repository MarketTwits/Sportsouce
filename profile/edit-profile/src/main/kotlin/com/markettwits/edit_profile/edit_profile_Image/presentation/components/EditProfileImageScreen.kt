package com.markettwits.edit_profile.edit_profile_Image.presentation.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import com.markettwits.edit_profile.edit_profile_Image.presentation.component.EditProfileImageComponent
import com.markettwits.edit_profile.edit_profile_Image.presentation.components.image_picker.uriToFile
import com.markettwits.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore

@Composable
fun EditProfileImageScreen(component: EditProfileImageComponent) {
    Dialog(onDismissRequest = {
        component.obtainEvent(EditProfileImageStore.Intent.Dismiss)
    }) {
        val state by component.state.collectAsState()
        var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
        val context = LocalContext.current
        val contract = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            selectedImageUri = uri
            val file = selectedImageUri?.let { uriToFile(context, it) }
            if (file != null) {
                component.obtainEvent(EditProfileImageStore.Intent.UpdateImage(file))
            }
        }
        EditProfileImageScreenContent(
            state = state,
            dismiss = { component.obtainEvent(EditProfileImageStore.Intent.Dismiss) },
            onClickImageBox = {
                contract.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            })
    }
}