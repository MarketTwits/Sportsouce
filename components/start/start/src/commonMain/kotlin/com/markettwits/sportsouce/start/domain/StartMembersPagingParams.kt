package com.markettwits.sportsouce.start.domain

// Parameters used to build server-side filters
data class StartMembersPagingParams(
    val query: String = "",
    val distances: List<Int> = emptyList(),
    val genders: List<String> = emptyList(),
)