package com.markettwits.shop.domain.mapper

/**
 * Converts a price from an integer format (in kopecks) to a formatted string with spaces as thousand separators.
 *
 * @param price The price in kopecks (e.g., 1234567 for 12345.67 rubles).
 * @return A string representing the price in rubles with thousand separators (e.g., "12 345.67").
 */
fun mapCloudPriceToDouble(price: Int): String =
    formatPriceWithSpaces(price / 100)

/**
 * Converts a price from a string format (in kopecks) to a formatted string with spaces as thousand separators.
 *
 * @param price The price in kopecks as a string (e.g., "1234567" for 12345.67 rubles).
 * @return A string representing the price in rubles with thousand separators (e.g., "12 345.67").
 */
fun mapCloudPriceToDouble(price: String): String =
    formatPriceWithSpaces(price.toInt() / 100)

/**
 * Formats an integer by adding spaces as thousand separators.
 *
 * @param price The integer to be formatted (e.g., 1234567).
 * @return A string with the formatted number, where thousands are separated by spaces (e.g., "1 234 567").
 *         If the provided `price` is `null`, the function returns "0".
 */
fun formatPriceWithSpaces(price: Int?): String {
    if (price == null) return "0"
    val priceStr = price.toString()
    val length = priceStr.length
    val result = StringBuilder()
    for (i in 0 until length) {
        if ((length - i) % 3 == 0 && i != 0) {
            result.append(' ')
        }
        result.append(priceStr[i])
    }

    return result.toString()
}