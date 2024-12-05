package com.markettwits.crashlitics.api.tracker

/**
 * Interface defining a contract for tracking exceptions.
 */
interface ExceptionTracker  {

    /**
     * Reports an exception with an optional key for categorization or context.
     *
     * @param exception The exception to be reported.
     * @param key An optional key providing additional context for the exception.
     */
    fun reportException(exception: Throwable, key: String? = null)

    /**
     * Logs a message to the tracker.
     *
     * @param message The message to be logged.
     */
    fun setLog(message: String)

    /**
     * Sets a user identifier in the tracker for associating logs or reports with a specific user.
     *
     * @param userId The identifier of the user.
     */
    fun setUserId(userId: String)

    /**
     * Adds a key-value pair as additional context in the tracker.
     *
     * @param key A pair of strings representing the key and its associated value.
     */
    fun setKey(key: Pair<String, String>)
}