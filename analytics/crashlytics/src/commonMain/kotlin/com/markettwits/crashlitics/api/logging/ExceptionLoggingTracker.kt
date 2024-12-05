package com.markettwits.crashlitics.api.logging

import com.markettwits.core.log.LogTagProvider
import com.markettwits.crashlitics.api.tracker.ExceptionTracker

/**
 * Interface representing a combination of a log tag provider and an exception tracker.
 * Provides a unified contract for logging and reporting exceptions with additional context.
 */
interface ExceptionLoggingTracker : LogTagProvider{
    /**
     * The exception tracker used to report exceptions.
     */
    val exceptionTracker : ExceptionTracker
}