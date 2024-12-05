package com.markettwits.time


/**
 * The `TimeMapper` provides methods for mapping and formatting date and time values.
 */
interface TimeMapper {

    /**
     * Maps and formats a given ISO 8601 time string into a specific time pattern.
     *
     * @param timePattern The desired {@link TimePattern} to format the time string into.
     * @param time A string representing a time in ISO 8601 format (e.g., "2024-12-18T15:30:00Z").
     * @return A formatted string representing the date and time in the specified pattern.
     *
     * @throws IllegalArgumentException If the time string cannot be parsed or the pattern is invalid.
     */
    fun mapTime(timePattern: TimePattern, time: String): String

    /**
     * Converts a given time string from a specific pattern into a UTC-compatible ISO 8601 format.
     *
     * This method supports patterns defined in {@link TimePattern}, such as `Full`, `FullWithEmptySpace`,
     * and `FullWithDots`. If the input time string does not contain a time component (e.g., "20.05.2024"),
     * it defaults the time to `00:00`.
     *
     * @param timePattern The {@link TimePattern} that describes the format of the input time string.
     * @param time A string representing the date and/or time in the format defined by the `timePattern`.
     * @return A string representing the date and time in ISO 8601 UTC format (e.g., "2024-12-18T00:00:00Z").
     *
     * @throws IllegalArgumentException If the time string cannot be parsed or the pattern is invalid.
     */
    fun mapTimeToCloud(timePattern: TimePattern = TimePattern.FullWithDots, time: String): String
}

