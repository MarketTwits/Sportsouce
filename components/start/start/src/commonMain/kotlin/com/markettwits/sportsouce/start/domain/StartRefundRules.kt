package com.markettwits.sportsouce.start.domain

data class StartRefundRules(
    val startId: Int,
    val startDate: String,
    val source: Source,
    val rules: List<Rule>,
) {
    enum class Source { Global, Start, Unknown }

    data class Rule(
        val id: Int,
        val refundPercent: Int,
        val daysBeforeStart: Int,
        val description: String,
    )
}
