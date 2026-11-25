package com.markettwits.sportsouce.notification.impl.push

import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailabilityLight
import com.google.firebase.messaging.FirebaseMessaging
import com.markettwits.sportsouce.notification.impl.domain.PushRegistrationPlatform
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import kotlin.time.Instant

internal class AndroidPushPlatform(
    private val context: Context,
    private val display: PushNotificationDisplay,
    private val devicePreference: PushDevicePreference,
) : PushRegistrationPlatform {
    private val messaging by lazy { FirebaseMessaging.getInstance() }
    private val installationFile = File(context.noBackupFilesDir, "sportsauce-push-installation")

    override fun isSupported(): Boolean =
        GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS

    override fun canShowNotifications(): Boolean = display.canShow()

    override fun setAutoInit(enabled: Boolean) {
        if (messaging.isAutoInitEnabled != enabled) messaging.isAutoInitEnabled = enabled
    }

    @Suppress("DEPRECATION")
    override suspend fun token(): String = messaging.token.await()

    override suspend fun installationId(create: Boolean): String? = withContext(Dispatchers.IO) {
        val saved = if (installationFile.isFile) installationFile.readText() else null
        saved?.takeIf { runCatching { UUID.fromString(it).toString() == it }.getOrDefault(false) }
            ?: if (create) UUID.randomUUID().toString().also(installationFile::writeText) else null
    }

    override fun dismissAccountNotifications() = display.dismissAll()

    override fun registeredAt(installationId: String, userId: Int, token: String) =
        devicePreference.registeredAt(installationId, userId, token)

    override fun saveRegistration(installationId: String, userId: Int, token: String, registeredAt: Instant) =
        devicePreference.saveRegistration(installationId, userId, token, registeredAt)

    override fun clearRegistration() = devicePreference.clearRegistration()
}
