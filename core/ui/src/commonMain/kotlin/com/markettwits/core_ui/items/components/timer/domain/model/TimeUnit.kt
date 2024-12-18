package com.markettwits.core_ui.items.components.timer.domain.model

data class TimeUnit(
    val leftDigit: Int = 0,
    val rightDigit: Int = 0,
){
    override fun toString(): String = "$leftDigit$rightDigit"
}
