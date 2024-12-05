
package com.markettwits.crashlitics.api.logging

import com.markettwits.core.log.errorLog

/**
 * Logs an error and reports it using the `ExceptionTracker`.
 *
 * @param error The exception to be reported.
 * @param logMessage A lambda providing the message to be logged.
 */
inline fun ExceptionLoggingTracker.errorShaker(error: Throwable, logMessage : () -> String){
    errorLog(error = error, logMessage = logMessage)
    exceptionTracker.setLog(logMessage())
    exceptionTracker.reportException(error, key = tag)
}

/**
 * Logs an error, sets additional context via a key-value pair, and reports the error using the `ExceptionTracker`.
 *
 * @param error The exception to be reported.
 * @param keys A pair of strings representing a key and its associated value for additional context.
 * @param logMessage A lambda providing the message to be logged.
 */
inline fun ExceptionLoggingTracker.errorShaker(error: Throwable, keys : Pair<String,String>, logMessage : () -> String){
    errorLog(error = error, logMessage = logMessage)
    exceptionTracker.setLog(logMessage())
    exceptionTracker.setKey(keys)
    exceptionTracker.reportException(error, key = tag)
}
