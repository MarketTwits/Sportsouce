package com.markettwits.crashlitics.api.di

import com.markettwits.crashlitics.api.tracker.AnalyticsTracker
import com.markettwits.crashlitics.tracker.TracerAnalyticsTracker
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val crashlyticsModule: Module = module {
    singleOf(::TracerAnalyticsTracker) bind AnalyticsTracker::class
}