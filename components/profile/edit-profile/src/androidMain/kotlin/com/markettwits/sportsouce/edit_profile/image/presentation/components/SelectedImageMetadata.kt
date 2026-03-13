package com.markettwits.sportsouce.edit_profile.image.presentation.components

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

internal data class SelectedImageMetadata(
    val fileName: String?,
    val contentType: String?,
)

internal fun Context.selectedImageMetadata(uri: Uri): SelectedImageMetadata {
    val contentType = contentResolver.getType(uri)
    var fileName: String? = null

    contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (nameIndex >= 0 && cursor.moveToFirst()) {
            fileName = cursor.getString(nameIndex)
        }
    }

    return SelectedImageMetadata(
        fileName = fileName,
        contentType = contentType
    )
}
