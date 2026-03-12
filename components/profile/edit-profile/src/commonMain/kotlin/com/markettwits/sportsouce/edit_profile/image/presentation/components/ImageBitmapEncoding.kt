package com.markettwits.sportsouce.edit_profile.image.presentation.components

import androidx.compose.ui.graphics.ImageBitmap

internal enum class ImageBitmapEncodingFormat {
    JPEG, PNG
}

internal expect fun ImageBitmap.encodeToByteArray(
    format: ImageBitmapEncodingFormat,
    quality: Int,
): ByteArray
