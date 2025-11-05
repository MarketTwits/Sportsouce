package com.markettwits.capturable

import androidx.compose.ui.graphics.ImageBitmap

expect fun ImageBitmap.toByteArray(
    compressionFormat: CompressionFormat,
    quality: Int
): ByteArray

enum class CompressionFormat {
    JPEG, PNG
}

enum class QualityFormat(val value: Int) {
    MAX(100),
    NORMAL(75),
    HALF(50)
}