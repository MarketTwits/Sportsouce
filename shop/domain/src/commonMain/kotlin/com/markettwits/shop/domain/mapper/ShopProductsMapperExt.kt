package com.markettwits.shop.domain.mapper

fun mapCloudPriceToDouble(price: Int): String =
    formatPrice(price / 100)

fun mapCloudPriceToDouble(price: String): String =
    formatPrice(price.toInt() / 100)

fun formatPrice(price: Int?): String {
    if (price == null) return "0"
    return runCatching {
        val integerPart = (price / 100).toString()
        val formatted = buildString {
            var count = 0
            for (i in integerPart.indices.reversed()) {
                if (count == 3) {
                    append(' ')
                    count = 0
                }
                append(integerPart[i])
                count++
            }
        }.reversed()
        formatted
    }.getOrDefault("0")
}