package com.markettwits

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log

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