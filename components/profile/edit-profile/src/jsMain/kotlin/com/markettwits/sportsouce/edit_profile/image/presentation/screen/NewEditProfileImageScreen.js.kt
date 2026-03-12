package com.markettwits.sportsouce.edit_profile.image.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.window.Dialog
import com.attafitamim.krop.core.crop.CropResult
import com.attafitamim.krop.core.crop.crop
import com.attafitamim.krop.core.crop.rememberImageCropper
import com.markettwits.sportsouce.edit_profile.image.presentation.component.EditProfileImageComponent
import com.markettwits.sportsouce.edit_profile.image.presentation.components.*
import com.markettwits.sportsouce.edit_profile.image.presentation.store.EditProfileImageStore
import kotlinx.coroutines.launch
import org.jetbrains.skia.Image
import org.khronos.webgl.ArrayBuffer
import org.w3c.files.FileReader

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
                openImageFileDialog { file ->
                    val metadata = file.selectedImageMetadata()
                    val reader = FileReader()
                    reader.onload = {
                        val byteArray = (reader.result as ArrayBuffer).toByteArray()
                        val lastModified = file.lastModified.toLong()
                        val imageBitmap = Image.makeFromEncoded(byteArray).toComposeImageBitmap()
                        scope.launch {
                            val result = imageCropper.crop(bmp = imageBitmap)
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
                    reader.onerror = {
                        console.error("Ошибка при чтении файла:", reader.error)
                    }
                    reader.readAsArrayBuffer(file)
                }
            }
        )
    }
}
