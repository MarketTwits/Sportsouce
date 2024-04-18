package com.markettwits.crashlitics.configuration

import kotlin.reflect.KProperty

class AnalyticsConfigurationBase : AnalyticsConfigurationAbstract() {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): AnalyticsConfigurationAbstract =
        AnalyticsConfigurationBase()
}