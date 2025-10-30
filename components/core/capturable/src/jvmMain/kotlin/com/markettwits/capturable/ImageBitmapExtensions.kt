package com.markettwits.capturable

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toAwtImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

actual fun ImageBitmap.toByteArray(
    compressionFormat: CompressionFormat,
    quality: Int
): ByteArray {
    val awtImage = this.toAwtImage()
    val format = when (compressionFormat) {
        CompressionFormat.JPEG -> "jpg"
        CompressionFormat.PNG -> "png"
    }

    val outputStream = ByteArrayOutputStream()
    ImageIO.write(awtImage, format, outputStream)
    return outputStream.toByteArray()
}