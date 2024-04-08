package com.markettwits.edit_profile.edit_profile_Image.presentation.components.image_picker.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import java.io.File


fun uriToFile(context: Context, uri: Uri): File? {
    val filePath = uriToFilePath(context, uri)
    return if (filePath != null) File(filePath) else null
}

private fun uriToFilePath(context: Context, uri: Uri): String? {
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    return cursor?.use {
        if (it.moveToFirst()) {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.getString(columnIndex)
        } else {
            null
        }
    }
}