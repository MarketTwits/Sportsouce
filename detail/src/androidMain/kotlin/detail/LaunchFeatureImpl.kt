package detail

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import detail.domain.LaunchFeature

class LaunchFeatureImpl(private val context : Context) : LaunchFeature {

    override fun sendEmail(email: String) {
        val callIntent: Intent = Uri.fromParts("mailto",email, null).let { i ->
            Intent(Intent.ACTION_SENDTO, i).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(callIntent)
        } catch (_: ActivityNotFoundException) {
            Log.e("Shift app", "Ошибка при открытие почты")
        }
    }

    override fun openMap(coordinates: String) {
        val callIntent: Intent = Uri.parse("geo:0,0?q=${coordinates}").let { i ->
            Intent(Intent.ACTION_VIEW, i).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(callIntent)
        } catch (_: ActivityNotFoundException) {
            Log.e("Shift app", "Ошибка при открытие почты")
        }
    }

    override fun openGeoOnMap(latitude: String) {
        val callIntent: Intent = Uri.parse("geo:${latitude}").let { i ->
            Intent(Intent.ACTION_VIEW, i).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(callIntent)
        } catch (_: ActivityNotFoundException) {
            Log.e("Shift app", "Ошибка при открытие почты")
        }
    }

    override fun openPhone(phone: String) {
        val callIntent: Intent = Uri.fromParts("tel",phone.filter { it.isDigit() }, null).let { number ->
            Intent(Intent.ACTION_DIAL, number).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(callIntent)
        } catch (_: ActivityNotFoundException) {
            Log.e("Shift app", "Ошибка при открытие телефона")
        }
    }
}