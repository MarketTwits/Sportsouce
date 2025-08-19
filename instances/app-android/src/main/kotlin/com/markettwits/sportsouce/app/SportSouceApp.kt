package com.markettwits.sportsouce.app

import android.app.Application
import com.markettwits.activityholder.CurrentActivityHolder
import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageFileDirectory
import com.markettwits.crashlitics.configuration.AnalyticsConfiguration
import com.markettwits.crashlitics.configuration.AnalyticsConfigurationBase
import com.markettwits.initKoin
import com.markettwits.sportsauce.deeplink.impl.di.deepLinkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext

class SportSouceApp : Application(), AnalyticsConfiguration by AnalyticsConfigurationBase() {

    override fun onCreate() {
        super.onCreate()
        CurrentActivityHolder.register(this)
        InStorageCacheDirectory.path = cacheDir.path
        InStorageFileDirectory.path = filesDir.path

        if (GlobalContext.getOrNull() == null) {
            try {
                initKoin(
                    modules = listOf(deepLinkModule)
                ) {
                    androidContext(this@SportSouceApp)
                }
            } catch (_: Exception) {
                // Koin already started, continue
            }
        }
    }
}