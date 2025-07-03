package com.markettwits.sportsouce.start.presentation.result.model

enum class GenderFilter(val displayName: String, val value: String?) {
    ALL("All", null),
    MALE("Male", "M"),
    FEMALE("Female", "F")
}

enum class SortBy(val displayName: String) {
    NAME("Name"),
    RESULT("Result"),
    START_NUMBER("Start Number")
}

enum class SortOrder {
    ASC, DESC
}

// Filter Data Classes
data class GroupFilter(
    val name: String,
    val isSelected: Boolean
)

data class TeamFilter(
    val name: String,
    val isSelected: Boolean
)

data class DistanceFilter(
    val name: String,
    val isSelected: Boolean
)

data class FilterState(
    val genderFilter: GenderFilter = GenderFilter.ALL,
    val distanceFilters: List<DistanceFilter> = emptyList(),
    val groupFilters: List<GroupFilter> = emptyList(),
    val teamFilters: List<TeamFilter> = emptyList(),
    val sortBy: SortBy = SortBy.NAME,
    val sortOrder: SortOrder = SortOrder.ASC,
    val searchQuery: String = ""
)