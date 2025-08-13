package com.markettwits.sportsouce.start.data.start

import com.markettwits.core.log.LogTagProvider
import com.markettwits.core.log.infoLog
import com.markettwits.core.paging.OffsetAndLimitPagingSourceNew
import com.markettwits.sportsouce.start.cloud.api.start.SportSauceStartApi
import com.markettwits.sportsouce.start.cloud.model.members.StartMember
import com.markettwits.sportsouce.start.domain.StartMembersPagingParams

internal const val START_MEMBERS_ITEMS_PAGE_SIZE = 20

class StartMembersPagingSource(
    private val startId: Int,
    private val startNetworkApi: SportSauceStartApi,
    private val params: StartMembersPagingParams,
) : OffsetAndLimitPagingSourceNew<StartMember>(START_MEMBERS_ITEMS_PAGE_SIZE),
    LogTagProvider {

    override val tag: String = "StartMembersPagingSource"

    private var totalItemsCount = 0

    override suspend fun getTotalCount(): Int = totalItemsCount

    override suspend fun load(offset: Int, limit: Int): List<StartMember> {
        val items = startNetworkApi.membersFiltered(
            startId = startId,
            filterText = params.query,
            distances = params.distances,
            genders = params.genders,
            skipCount = offset,
            maxResultCount = limit
        )
        infoLog { "Start members load offset $offset limit $limit" }
        totalItemsCount = items.count
        return items.rows
    }
}