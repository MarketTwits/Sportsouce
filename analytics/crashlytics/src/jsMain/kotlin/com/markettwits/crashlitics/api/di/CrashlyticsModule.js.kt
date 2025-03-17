package com.markettwits.crashlitics.api.di

import com.markettwits.crashlitics.api.tracker.EmptyExceptionTracker
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val crashlyticsModule: Module =  module {
    singleOf(::EmptyExceptionTracker) bind ExceptionTracker::class
}