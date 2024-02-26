package com.markettwits.registrations.registrations.presentation.components.filter

data class FilterItem(val value: String, val checked: Boolean)

val baseFilter = listOf(
    FilterItem("Оплачено", false),
    FilterItem("Регистрация бесплатна", false),
    FilterItem("Оплата отменена", false),
    FilterItem("Не оплачено", false),
)