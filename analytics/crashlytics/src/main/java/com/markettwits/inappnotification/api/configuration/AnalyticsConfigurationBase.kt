package com.markettwits.inappnotification.api.configuration

import kotlin.reflect.KProperty

class AnalyticsConfigurationBase : AnalyticsConfigurationAbstract() {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): AnalyticsConfigurationAbstract =
        AnalyticsConfigurationBase()
}