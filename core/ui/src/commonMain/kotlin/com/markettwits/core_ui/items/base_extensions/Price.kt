package com.markettwits.core_ui.items.base_extensions

//import java.text.NumberFormat
//import java.util.Locale

fun Int?.formatPrice(): String =
    runCatching {
//        val formatter = NumberFormat.getInstance(Locale("ru", "RU"))
//            if (this == null) "0"
//        else
//            formatter.format(this / 100)
    }.fold(onSuccess = { it.toString() }, onFailure = { "0" })