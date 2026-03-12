package com.markettwits.sportsouce.edit_profile.image.presentation.components

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toAwtImage
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

internal actual fun ImageBitmap.encodeToByteArray(
    format: ImageBitmapEncodingFormat,
    quality: Int,
): ByteArray {
    val source = toAwtImage()
    val buffered = when (format) {
        ImageBitmapEncodingFormat.JPEG -> BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        ImageBitmapEncodingFormat.PNG -> BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    }

    val graphics = buffered.createGraphics()
    if (format == ImageBitmapEncodingFormat.JPEG) {
        graphics.color = Color.WHITE
        graphics.fillRect(0, 0, width, height)
    }
    graphics.drawImage(source, 0, 0, null)
    graphics.dispose()

    val outputStream = ByteArrayOutputStream()
    val outputFormat = when (format) {
        ImageBitmapEncodingFormat.JPEG -> "jpg"
        ImageBitmapEncodingFormat.PNG -> "png"
    }
    ImageIO.write(buffered, outputFormat, outputStream)
    return outputStream.toByteArray()
}
