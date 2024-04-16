package com.markettwits.edit_profile.edit_profile_Image.presentation.components.image_picker.utils

import okio.buffer
import okio.sink
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

//fun convertBitmapToFile(context: Context, imageBitmap: Bitmap): File {
//    val directory = context.getDir("Images", Context.MODE_PRIVATE)
//    val file = File(directory, "${UUID.randomUUID()}.jpg")
//    val stream: OutputStream = BufferedOutputStream(FileOutputStream(file))
//    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
//    stream.close()
//    return file
//}
fun byteArrayToFile(byteArray: ByteArray, fileName: String): File? {
    return try {
        val tempFile = kotlin.io.path.createTempFile(fileName, null)
        tempFile.sink().buffer().use { sink ->
            sink.write(byteArray)
        }
        tempFile.toFile()
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

fun writeByteArrayToFile(byteArray: ByteArray, filePath: String): Boolean {
    return try {
        val outputStream = FileOutputStream(filePath)
        outputStream.write(byteArray)
        outputStream.close()
        true
    } catch (e: Exception) {
        false
    }
}