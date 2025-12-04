package com.markettwits.core_ui.items.extensions

fun String.isStrictUrl(): Boolean {
    val regex = Regex(
        pattern = "^https?://[\\w.-]+(?:\\.[\\w.-]+)+[/\\w.,@?^=%&:/~+#-]*$",
        options = setOf(RegexOption.IGNORE_CASE)
    )
    return regex.matches(this.trim())
}

fun String.isLooseUrl(): Boolean {
    val regex = Regex(
        pattern = "^(https?://)?[\\w.-]+(?:\\.[\\w.-]+)+[/\\w.,@?^=%&:/~+#-]*$",
        options = setOf(RegexOption.IGNORE_CASE)
    )
    return regex.matches(this.trim())
}