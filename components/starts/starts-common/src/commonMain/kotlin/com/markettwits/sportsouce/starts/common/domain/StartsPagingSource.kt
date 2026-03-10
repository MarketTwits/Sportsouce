package com.markettwits.sportsouce.starts.common.domain

import com.markettwits.core.paging.OffsetAndLimitPagingSourceNew

const val STARTS_PAGE_SIZE = 20

data class StartsPagingParams(
    val isMain: Boolean? = null,
    val openFirst: Boolean? = null,
    val isGroup: Boolean? = null,
    val statuses: List<StartStatus> = emptyList(),
    val additionalParameters: Map<String, String> = emptyMap(),
)

class StartsPagingSource(
    private val startsApi: SportSauceStartsApi,
    private val params: StartsPagingParams,
    pageSize: Int = STARTS_PAGE_SIZE,
) : OffsetAndLimitPagingSourceNew<StartsListItem>(pageSize = pageSize) {

    override suspend fun getTotalCount(): Int = 0

    override suspend fun load(offset: Int, limit: Int): List<StartsListItem> {
        return startsApi.fetchStarts(
            limit = limit,
            offset = offset,
            isMain = params.isMain,
            openFirst = params.openFirst,
            isGroup = params.isGroup,
            statuses = params.statuses,
            additionalParameters = params.additionalParameters,
        )
    }
}
