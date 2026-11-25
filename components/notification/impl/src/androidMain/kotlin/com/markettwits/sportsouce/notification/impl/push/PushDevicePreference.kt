package com.markettwits.sportsouce.notification.impl.push

import android.content.Context
import com.markettwits.core_cloud.provider.SportSauceBaseUrl
import java.security.MessageDigest
import kotlin.time.Instant

internal class PushDevicePreference(context: Context) {
    private val preferences = context.getSharedPreferences("sportsauce.notifications", Context.MODE_PRIVATE)

    var enabled: Boolean
        get() = preferences.getBoolean(PUSH_ENABLED, true)
        set(value) {
            preferences.edit().putBoolean(PUSH_ENABLED, value).apply()
        }

    fun registeredAt(installationId: String, userId: Int, token: String): Instant? {
        val record = preferences.getString(REGISTRATION, null).orEmpty()
        val prefix = registrationKey(installationId, userId, token)
        if (!record.startsWith(prefix)) return null
        return record.removePrefix(prefix).toLongOrNull()?.let(Instant::fromEpochMilliseconds)
    }

    fun saveRegistration(installationId: String, userId: Int, token: String, at: Instant) {
        val key = registrationKey(installationId, userId, token)
        preferences.edit().putString(REGISTRATION, key + at.toEpochMilliseconds()).apply()
    }

    fun clearRegistration() {
        preferences.edit().remove(REGISTRATION).apply()
    }

    private fun registrationKey(installationId: String, userId: Int, token: String): String {
        val input = "$SportSauceBaseUrl\n$installationId\n$userId\n$token"
        val digest = MessageDigest.getInstance("SHA-256").digest(input.encodeToByteArray())
        return "${digest.toHexString()}:"
    }

    private companion object {
        const val PUSH_ENABLED = "push_enabled"
        const val REGISTRATION = "push_registration"
    }
}
