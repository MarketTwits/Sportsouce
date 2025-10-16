package com.markettwits.core_ui.items.extensions


/**
 * Форматирует цену в копейках с разделением тысяч пробелами.
 *
 * Преобразует целочисленное значение копеек в строку с рублями,
 * разделяя тысячи пробелами для улучшения читаемости.
 *
 * @receiver Int? цена в копейках (100 копеек = 1 рубль)
 * @return отформатированная строка с рублями
 *
 * Примеры:
 * ```
 * 400000.formatPrice() // "4 000"
 * 123456789.formatPrice() // "1 234 567"
 * 50000.formatPrice() // "500"
 * null.formatPrice() // "0"
 * ```
 */
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

/**
 * Форматирует цену в рублях с разделением тысяч пробелами.
 *
 * Преобразует целочисленное значение рублей в строку,
 * разделяя тысячи пробелами для улучшения читаемости.
 *
 * @receiver Int? цена в рублях
 * @return отформатированная строка с рублями
 *
 * Примеры:
 * ```
 * 4000.formatRubles() // "4 000"
 * 50000.formatRubles() // "50 000"
 * 1234567.formatRubles() // "1 234 567"
 * 500.formatRubles() // "500"
 * null.formatRubles() // "0"
 * ```
 */
fun Int?.formatRubles(): String {
    if (this == null) return "0"
    return runCatching {
        val rubles = this.toString()
        val formatted = buildString {
            var count = 0
            for (i in rubles.indices.reversed()) {
                if (count == 3) {
                    append(' ')
                    count = 0
                }
                append(rubles[i])
                count++
            }
        }.reversed()
        formatted
    }.getOrDefault("0")
}