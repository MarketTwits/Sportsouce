package com.markettwits.sportsouce.start.presentation.result.model


enum class SortBy(val displayName: String) {
    RESULT("Результату"),
    NAME("Имени"),
    START_NUMBER("Стартовому номеру")
}

enum class SortOrder {
    ASC, DESC
}

data class GroupFilter(
    val name: String,
    val isSelected: Boolean
)

data class DistanceFilter(
    val name: String,
    val isSelected: Boolean
)

data class GenderFilter(
    val name: String,
    val isSelected: Boolean,
)

data class FilterState(
    val distanceFilters: List<DistanceFilter> = emptyList(),
    val groupFilters: List<GroupFilter> = emptyList(),
    val genderFilters: List<GenderFilter> = emptyList(),
    val sortBy: SortBy = SortBy.RESULT,
    val sortOrder: SortOrder = SortOrder.ASC,
    val searchQuery: String = "",
)

internal fun List<DistanceFilter>.getSelectDistance() : DistanceFilter?
    = this.firstOrNull { it.isSelected }

internal fun List<GroupFilter>.getSelectedGroups() : List<GroupFilter>
        = this.filter { it.isSelected }

internal fun List<GenderFilter>.getSelectedGenders(): List<GenderFilter> = this.filter { it.isSelected }
