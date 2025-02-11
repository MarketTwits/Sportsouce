package com.markettwits.core_ui.items.base_extensions


fun Int?.formatPrice(): String {
    return runCatching {
        if (this == null) "0"
        else String.format("%,d", this / 100).replace(',', ' ')
    }.getOrDefault("0")
}