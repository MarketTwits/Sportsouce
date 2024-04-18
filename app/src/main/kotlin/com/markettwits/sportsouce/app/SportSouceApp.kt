package com.markettwits.sportsouce.app

import android.app.Application
import com.markettwits.crashlitics.configuration.AnalyticsConfiguration
import com.markettwits.crashlitics.configuration.AnalyticsConfigurationBase

class SportSouceApp : Application(), AnalyticsConfiguration by AnalyticsConfigurationBase()