package com.markettwits

import android.content.*
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

class IntentActionBase(
    private val context: Context,
) : IntentAction {
    override fun openWebPage(url: String) {
        try {
            val webpage =
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    Uri.parse("http://$url")
                } else Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, webpage).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e("mt05", "Ошибка при открытии веб страницы")
        }
    }

    override fun openPhone(phone: String) {
        val callIntent: Intent =
            Uri.fromParts("tel", phone.filter { it.isDigit() }, null).let { number ->
                Intent(Intent.ACTION_DIAL, number).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        try {
            context.startActivity(callIntent)
        } catch (_: ActivityNotFoundException) {
            Log.e("mt05", "Ошибка при открытие телефона")
        }
    }


    override fun copyToSystemBuffer(text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Скопировано в буфер обмена", Toast.LENGTH_SHORT).show()
    }

    override fun shareImage(byteArray: ByteArray, filename: String) {
        val context = context.applicationContext
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs()

        val file = File(cachePath, "$filename.png")
        FileOutputStream(file).use { it.write(byteArray) }

        val contentUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, contentUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(
            Intent.createChooser(shareIntent, "Share image via")
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    override fun saveImage(byteArray: ByteArray, filename: String) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, "$filename.png")
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }

                val resolver = context.contentResolver
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

                uri?.let {
                    resolver.openOutputStream(it)?.use { outputStream ->
                        outputStream.write(byteArray)
                    }
                    Toast.makeText(
                        context,
                        "Изображение сохранено в Downloads",
                        Toast.LENGTH_LONG
                    ).show()
                } ?: throw Exception("Failed to create MediaStore entry")
            } else {
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                downloadsDir.mkdirs()

                val file = File(downloadsDir, "$filename.png")
                var counter = 1
                var finalFile = file

                while (finalFile.exists()) {
                    finalFile = File(downloadsDir, "$filename($counter).png")
                    counter++
                }

                FileOutputStream(finalFile).use { it.write(byteArray) }

                Toast.makeText(
                    context,
                    "Изображение сохранено в Downloads: ${finalFile.name}",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            Log.e("mt05", "Ошибка при сохранении изображения", e)
            Toast.makeText(
                context,
                "Ошибка при сохранении изображения",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun sharePlainText(text: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val shareIntent =
            Intent
                .createChooser(sendIntent, null)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }
}