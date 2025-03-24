package com.markettwits.core_ui.items.extensions


fun Int?.formatPrice(): String {
    if (this == null) return "0"
    return runCatching {
        val integerPart = (this / 100).toString()
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