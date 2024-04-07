package com.markettwits.inappnotification.api.di

import com.markettwits.inappnotification.api.tracker.AnalyticsTracker
import com.markettwits.inappnotification.impl.TracerAnalyticsTracker
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val crashlyticsModule = module {
    singleOf(::TracerAnalyticsTracker) bind AnalyticsTracker::class
}