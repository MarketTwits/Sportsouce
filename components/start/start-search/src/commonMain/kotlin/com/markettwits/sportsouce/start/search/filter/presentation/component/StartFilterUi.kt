package com.markettwits.sportsouce.start.search.filter.presentation.component

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class StartFilterUi(
    val items: List<FilterGroupItemUi>,
    val fromDate: LocalDate? = null,
    val toDate: LocalDate? = null,
) {
    val hasInvalidPeriod: Boolean
        get() = fromDate != null && toDate != null && fromDate > toDate

    val activeCount: Int
        get() = items.count { it.selected.isNotEmpty() } +
            if (fromDate != null || toDate != null) 1 else 0

    fun filterIsEmpty(): Boolean = activeCount == 0

    fun cleared(): StartFilterUi =
        copy(items = items.map { it.copy(selected = emptyList()) }, fromDate = null, toDate = null)

    fun item(key: Key): FilterGroupItemUi? = items.firstOrNull { it.key == key || (it.key == null && it.label == key.label) }

    @Serializable
    enum class Key(val label: String) {
        Sport("Вид спорта"),
        Season("Сезон"),
        City("Место проведения"),
        Organizer("Организатор"),
        Status("Актуальность"),
        Sort("Сортировка"),
    }

    @Serializable
    data class FilterGroupItemUi(
        val label: String,
        val type: FilterStartType,
        val selected: List<String> = emptyList(),
        val list: List<String>,
        val key: Key? = null,
    ) {
        fun checkSelected(item: String) = selected.contains(item)
    }

    @Serializable
    sealed interface FilterStartType {
        @Serializable
        data object Calendar : FilterStartType

        @Serializable
        data object DropDown : FilterStartType

        @Serializable
        data object Dialog : FilterStartType
    }

}