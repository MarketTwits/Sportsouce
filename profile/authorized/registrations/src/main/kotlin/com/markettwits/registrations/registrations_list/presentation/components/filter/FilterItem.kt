package com.markettwits.registrations.registrations_list.presentation.components.filter

data class FilterItem(val value: String, val checked: Boolean)

val baseFilter = listOf(
    FilterItem("Оплачено", false),
    FilterItem("Бесплатно", false),
    FilterItem("Оплата отменена", false),
    FilterItem("Не оплачено", false),
)