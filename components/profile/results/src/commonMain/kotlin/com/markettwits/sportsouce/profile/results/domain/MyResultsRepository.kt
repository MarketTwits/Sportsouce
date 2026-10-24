package com.markettwits.sportsouce.profile.results.domain

interface MyResultsRepository {
    suspend fun series(): List<ResultSeries>
    suspend fun summary(): ResultsSummary
    suspend fun feed(offset: Int, limit: Int, category: String? = null): ResultFeed
    suspend fun progress(category: String?, comparison: ResultComparison, series: String? = null): ResultProgress
}
