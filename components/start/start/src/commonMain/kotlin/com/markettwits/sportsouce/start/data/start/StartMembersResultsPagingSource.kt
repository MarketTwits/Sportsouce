package com.markettwits.sportsouce.start.data.start

import com.markettwits.core.log.LogTagProvider
import com.markettwits.core.log.infoLog
import com.markettwits.core.paging.OffsetAndLimitPagingSourceNew
import com.markettwits.sportsouce.start.cloud.api.start.SportSauceStartApi
import com.markettwits.sportsouce.start.cloud.model.result.v2.StartMemberResultV2
import com.markettwits.sportsouce.start.domain.StartMembersResultsPagingParams

internal class StartMembersResultsPagingSource(
    private val startId: Int,
    private val startNetworkApi: SportSauceStartApi,
    private val params: StartMembersResultsPagingParams = StartMembersResultsPagingParams(),
) : OffsetAndLimitPagingSourceNew<StartMemberResultV2>(START_MEMBERS_RESULTS_PAGE_SIZE),
    LogTagProvider {

    override val tag: String = "StartMembersResultsPagingSource"

    private var totalItemsCount = 0

    override suspend fun getTotalCount(): Int = totalItemsCount

    override suspend fun load(offset: Int, limit: Int): List<StartMemberResultV2> {
        val page = (offset / limit) + 1

        val response = startNetworkApi.membersResultsAnalyze(
            startId = startId,
            page = page,
            maxResultCount = limit,
            gender = params.gender,
            group = params.group,
            distance = params.distance,
            searchQuery = params.searchQuery
        )

        infoLog { "Start members results load page $page limit $limit, filters: gender=${params.gender}, group=${params.group}, distance=${params.distance}, searchQuery=${params.searchQuery}, total count: ${response.count}" }
        totalItemsCount = response.count

        return response.rows.mapIndexed { index, item ->
            item.copy(computedPlace = offset + index + 1)
        }
    }
}

internal const val START_MEMBERS_RESULTS_PAGE_SIZE = 20