package com.markettwits.edit_profile.edit_profile_Image.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.Dialog
import com.markettwits.edit_profile.edit_profile_Image.presentation.component.EditProfileImageComponent
import com.markettwits.edit_profile.edit_profile_Image.presentation.components.EditProfileImageScreenContent
import com.markettwits.edit_profile.edit_profile_Image.presentation.components.openImageFileDialog
import com.markettwits.edit_profile.edit_profile_Image.presentation.components.toByteArray
import com.markettwits.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore
import org.khronos.webgl.ArrayBuffer
import org.w3c.files.FileReader

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
                openImageFileDialog { file ->
                    val reader = FileReader()
                    reader.onload = {
                        val byteArray = (reader.result as ArrayBuffer).toByteArray()
                        val lastModified = file.lastModified.toLong()
                        component.obtainEvent(
                            EditProfileImageStore.Intent.UpdateImage(
                                byteArray,
                                lastModified
                            )
                        )
                    }
                    reader.onerror = {
                        console.error("Ошибка при чтении файла:", reader.error)
                    }
                    reader.readAsArrayBuffer(file)
                }
            }
        )
    }
}