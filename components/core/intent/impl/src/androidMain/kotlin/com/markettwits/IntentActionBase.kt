package com.markettwits

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
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

    override fun shareImage(byteArray: ByteArray) {
        val context = context.applicationContext
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs()

        val file = File(cachePath, "shared_image.png")
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