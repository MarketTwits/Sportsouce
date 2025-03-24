package com.markettwits.core.log

import co.touchlab.kermit.Logger

/**
 * Extension function for `LogTagProvider` to log error messages without exceptions.
 *
 * @param logMessage A lambda providing the message to be logged.
 */
inline fun LogTagProvider.errorLog(logMessage : () -> String){
    Logger.e(tag = tag, messageString = logMessage())
}

/**
 * Extension function for `LogTagProvider` to log error messages with exceptions.
 *
 * @param error The exception associated with the error.
 * @param logMessage A lambda providing the message to be logged.
 */
inline fun LogTagProvider.errorLog(error : Throwable, logMessage : () -> String){
    Logger.e(tag = tag, messageString = logMessage(), throwable = error)
}
/**
 * Extension function for `LogTagProvider` to log informational messages.
 *
 * @param logMessage A lambda providing the message to be logged.
 */
inline fun LogTagProvider.infoLog(logMessage : () -> String){
    Logger.i(tag = tag, messageString = logMessage())
}