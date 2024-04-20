package com.markettwits.core_ui.items.base_extensions

import java.text.SimpleDateFormat
import java.util.Date


fun Long.mapDateToString(): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy")
    val date = Date(this)
    return sdf.format(date)
}