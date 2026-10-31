package com.markettwits.selfupdater.thirdparty.api

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.markettwits.selfupdater.api.SelfUpdaterSourceApi
import com.markettwits.selfupdater.googleplay.api.SelfUpdaterGooglePlay
import com.markettwits.selfupdater.models.SelfUpdateResult
import com.markettwits.sportsouce.inappnotification.api.InAppNotificationStorage
import org.koin.dsl.module
import org.koin.core.context.GlobalContext

actual val selfUpdaterThirdPartyModule = module {

    single<SelfUpdaterSourceApi> {
        // Feature components use isolated Koin applications; Android context and
        // the distribution-specific factory are registered by the app itself.
        val context: Context = getOrNull() ?: GlobalContext.get().get()
        when (selectAndroidUpdateChannel(context.updateBuildChannel(), context.installerPackage())) {
            AndroidUpdateChannel.GooglePlay -> SelfUpdaterGooglePlay(context, get())
            AndroidUpdateChannel.GitHub -> (getOrNull<AndroidSideloadUpdaterFactory>()
                ?: GlobalContext.getOrNull()?.getOrNull<AndroidSideloadUpdaterFactory>())
                ?.create(context, get(), get()) ?: MissingSideloadUpdater
        }
    }
}

/** The application flavor supplies this binding only when APK installation is supported. */
fun interface AndroidSideloadUpdaterFactory {
    fun create(context: Context, parser: SelfUpdateParserApi, notifications: InAppNotificationStorage): SelfUpdaterSourceApi
}

private object MissingSideloadUpdater : SelfUpdaterSourceApi {
    override suspend fun checkUpdate(manual: Boolean): SelfUpdateResult = SelfUpdateResult.Unsupported
    override fun getInstallSourceName(): String = "GitHub"
    override fun isSelfUpdateCanManualCheck(): Boolean = false
}

private fun Context.updateBuildChannel(): String? = runCatching {
    val info = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getApplicationInfo(packageName, PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong()))
    } else {
        @Suppress("DEPRECATION")
        packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
    }
    info.metaData?.getString("com.markettwits.selfupdater.CHANNEL")
}.getOrNull()

@Suppress("DEPRECATION")
private fun Context.installerPackage(): String? = runCatching {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) packageManager.getInstallSourceInfo(packageName).installingPackageName
    else packageManager.getInstallerPackageName(packageName)
}.getOrNull()
