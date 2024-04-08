package com.markettwits.edit_profile.edit_profile_Image.presentation.components.image_picker.utils

import android.content.Context
import android.graphics.Bitmap
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.UUID

fun convertBitmapToFile(context: Context, imageBitmap: Bitmap): File {
    val directory = context.getDir("Images", Context.MODE_PRIVATE)
    val file = File(directory, "${UUID.randomUUID()}.jpg")
    val stream: OutputStream = BufferedOutputStream(FileOutputStream(file))
    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    stream.close()
    return file
}