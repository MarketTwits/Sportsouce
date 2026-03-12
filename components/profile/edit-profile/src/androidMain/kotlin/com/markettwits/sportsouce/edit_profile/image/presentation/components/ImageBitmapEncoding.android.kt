package com.markettwits.sportsouce.edit_profile.image.presentation.components

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import java.io.ByteArrayOutputStream

internal actual fun ImageBitmap.encodeToByteArray(
    format: ImageBitmapEncodingFormat,
    quality: Int,
): ByteArray {
    val androidBitmap: Bitmap = asAndroidBitmap()
    val stream = ByteArrayOutputStream()
    val compressFormat = when (format) {
        ImageBitmapEncodingFormat.JPEG -> Bitmap.CompressFormat.JPEG
        ImageBitmapEncodingFormat.PNG -> Bitmap.CompressFormat.PNG
    }
    androidBitmap.compress(compressFormat, quality, stream)
    return stream.toByteArray()
}
