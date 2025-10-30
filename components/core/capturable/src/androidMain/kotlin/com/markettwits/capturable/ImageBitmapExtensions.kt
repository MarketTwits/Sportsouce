package com.markettwits.capturable

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import java.io.ByteArrayOutputStream

actual fun ImageBitmap.toByteArray(
    compressionFormat: CompressionFormat,
    quality: Int
): ByteArray {
    val androidBitmap: Bitmap = this.asAndroidBitmap()
    val stream = ByteArrayOutputStream()
    val format = when (compressionFormat) {
        CompressionFormat.JPEG -> Bitmap.CompressFormat.JPEG
        CompressionFormat.PNG -> Bitmap.CompressFormat.PNG
    }
    androidBitmap.compress(format, quality, stream)
    return stream.toByteArray()
}