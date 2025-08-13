package com.markettwits.sportsouce.start.domain

/**
 * Domain representation of member filters
 */
data class FiltersRemote(
    val cities: List<FilterItem>,
    val distances: List<FilterItem>,
    val genders: List<FilterItem>,
    val groups: List<FilterItem>,
    val teams: List<FilterItem>,
)

/**
 * Unified filter item that can represent any filter type
 */
data class FilterItem(
    val value: String,
    val count: Int,
    val id: Int? = null, // Only used for distances
)