package com.markettwits.sportsouce.profile.results.data

import com.markettwits.sportsouce.auth.service.api.AuthDataSource
import com.markettwits.sportsouce.profile.results.domain.*

internal class MyResultsRepositoryBase(private val api: MyResultsApi, private val auth: AuthDataSource) : MyResultsRepository {
    override suspend fun series() = api.series(auth.updateToken().getOrThrow())
    override suspend fun summary() = api.summary(auth.updateToken().getOrThrow())
    override suspend fun feed(offset: Int, limit: Int, category: String?) = api.feed(auth.updateToken().getOrThrow(), offset, limit, category)
    override suspend fun progress(category: String?, comparison: ResultComparison, series: String?) = api.progress(auth.updateToken().getOrThrow(), category, comparison, series)
}
