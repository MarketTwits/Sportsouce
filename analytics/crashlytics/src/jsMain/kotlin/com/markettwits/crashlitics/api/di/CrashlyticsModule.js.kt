package com.markettwits.crashlitics.api.di

import com.markettwits.crashlitics.api.tracker.EmptyExceptionTracker
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

actual val crashlyticsModule: Module = module {
    singleOf(::EmptyExceptionTracker) bind ExceptionTracker::class
}