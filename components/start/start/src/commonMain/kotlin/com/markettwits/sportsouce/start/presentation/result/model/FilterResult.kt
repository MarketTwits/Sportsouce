package com.markettwits.sportsouce.start.presentation.result.model

interface MemberFilter<T> {
    val key: String
    val currentValue: T?
    fun reset()
    fun isActive(): Boolean
}

// Базовый класс для сортировки
interface MemberSorter {
    val currentOrder: SortOrder
    fun toggleOrder()
    fun reset()
    fun isActive(): Boolean
}

enum class SortOrder {
    ASC, DESC, NONE
}


enum class ResultStatus {
    COMPLETED, DNF, DSQ, DNS
}

enum class SortByResult {
    PLACE, NAME, TIME, TEAM
}

enum class GenderResult {
    ALL, MALE, FEMALE
}

enum class MemberResultFilterOption{
    GENDER,DISTANCE,CLUB,CITY
}

class MemberResultItem(
    val option: MemberResultFilterOption,
    val value: String,
)