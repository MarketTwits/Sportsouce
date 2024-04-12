package com.markettwits

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

class IntentActionBase(private val context: Context) : IntentAction {
    override fun openWebPage(url: String) {
        try {
            val webpage =
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    Uri.parse("http://$url")
                } else Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, webpage).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {

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

        }
    }
}