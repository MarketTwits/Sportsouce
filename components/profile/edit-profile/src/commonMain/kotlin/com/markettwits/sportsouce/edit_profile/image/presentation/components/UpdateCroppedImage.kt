package com.markettwits.sportsouce.edit_profile.image.presentation.components

import androidx.compose.ui.graphics.ImageBitmap
import com.markettwits.sportsouce.edit_profile.image.presentation.component.EditProfileImageComponent
import com.markettwits.sportsouce.edit_profile.image.presentation.store.EditProfileImageStore

internal fun EditProfileImageComponent.updateCroppedImage(
    bitmap: ImageBitmap,
    lastModified: Long,
    sourceFileName: String?,
    sourceContentType: String?,
) {
    val upload = croppedImageUploadFor(
        sourceFileName = sourceFileName,
        sourceContentType = sourceContentType
    )
    obtainEvent(
        EditProfileImageStore.Intent.UpdateImage(
            data = bitmap.encodeToByteArray(
                format = upload.compressionFormat,
                quality = 100
            ),
            lastModified = lastModified,
            fileName = upload.fileName,
            contentType = upload.contentType
        )
    )
}
