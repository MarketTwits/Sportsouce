package com.markettwits.sportsouce.profile.results.domain

import kotlinx.serialization.Serializable

@Serializable
data class ResultsSummary(
    val totalStarts: Int = 0,
    val finishes: Int = 0,
    val since: Int? = null,
    val categories: List<ResultCategory> = emptyList(),
    val pendingExternal: Int = 0,
)

@Serializable
data class ResultCategory(
    val key: String,
    val label: String,
    val sport: String? = null,
    val distanceM: Double? = null,
    val count: Int = 0,
    val best: PersonalBest? = null,
)

@Serializable
data class PersonalBest(val seconds: Double? = null, val startId: Int? = null, val startName: String = "", val date: String? = null)

@Serializable
data class ResultFeed(val count: Int = 0, val rows: List<MyResult> = emptyList())

@Serializable
data class MyResult(
    val key: String,
    val kind: String = "result",
    val source: String? = null,
    val startId: Int? = null,
    val startName: String = "",
    val date: String? = null,
    val category: String? = null,
    val categoryLabel: String? = null,
    val distance: String? = null,
    val resultSeconds: Double? = null,
    val resultText: String? = null,
    val status: String? = null,
    val place: Int? = null,
    val sexPlace: Int? = null,
    val finishers: Int? = null,
    val group: String? = null,
    val groupPlace: Int? = null,
    val groupFinishers: Int? = null,
    val bib: String? = null,
    val isTeam: Boolean = false,
    val fileUrl: String? = null,
    val comment: String? = null,
)

enum class ResultComparison(val apiValue: String, val label: String) {
    Self("self", "Только мои результаты"), Group("group", "С возрастной группой"), Age("age", "С участниками моего возраста")
}

@Serializable
data class ResultProgress(val mode: String = "time", val compare: String = "self", val points: List<ProgressPoint> = emptyList())

@Serializable
data class ProgressPoint(
    val key: String,
    val date: String? = null,
    val year: Int? = null,
    val startId: Int? = null,
    val startName: String = "",
    val category: String? = null,
    val categoryLabel: String? = null,
    val resultSeconds: Double? = null,
    val pace: Double? = null,
    val value: Double? = null,
    val place: Int? = null,
    val group: String? = null,
    val compare: ComparisonValue? = null,
)

@Serializable
data class ComparisonValue(val value: Double, val sample: Int = 0, val label: String = "")

@Serializable
data class ResultSeries(val key: String, val name: String, val count: Int = 0, val categories: List<String> = emptyList())
