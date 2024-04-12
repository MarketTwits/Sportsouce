package com.markettwits.sportsouce.app

import android.app.Application
import com.markettwits.inappnotification.api.configuration.AnalyticsConfiguration
import com.markettwits.inappnotification.api.configuration.AnalyticsConfigurationBase

class SportSouceApp : Application(), AnalyticsConfiguration by AnalyticsConfigurationBase()