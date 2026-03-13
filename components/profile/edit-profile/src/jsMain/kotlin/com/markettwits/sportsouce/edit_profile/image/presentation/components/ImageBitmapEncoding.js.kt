package com.markettwits.sportsouce.edit_profile.image.presentation.components

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image

internal actual fun ImageBitmap.encodeToByteArray(
    format: ImageBitmapEncodingFormat,
    quality: Int,
): ByteArray {
    val skiaBitmap = asSkiaBitmap()
    val image = Image.makeFromBitmap(skiaBitmap)
    val encodedFormat = when (format) {
        ImageBitmapEncodingFormat.JPEG -> EncodedImageFormat.JPEG
        ImageBitmapEncodingFormat.PNG -> EncodedImageFormat.PNG
    }
    return image.encodeToData(format = encodedFormat, quality = quality)?.bytes ?: ByteArray(0)
}
