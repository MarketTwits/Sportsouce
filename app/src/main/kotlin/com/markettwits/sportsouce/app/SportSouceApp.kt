package com.markettwits.sportsouce.app

import android.app.Application
import com.markettwits.activityholder.CurrentActivityHolder

import com.markettwits.crashlitics.configuration.AnalyticsConfiguration
import com.markettwits.crashlitics.configuration.AnalyticsConfigurationBase

class SportSouceApp : Application(), AnalyticsConfiguration by AnalyticsConfigurationBase() {

    override fun onCreate() {
        super.onCreate()
        CurrentActivityHolder.register(this)
    }
}