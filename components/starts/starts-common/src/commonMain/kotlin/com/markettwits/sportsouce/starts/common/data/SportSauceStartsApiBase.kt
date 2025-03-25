package com.markettwits.sportsouce.starts.common.data

import com.markettwits.sportsouce.starts.cloud.SportSauceNetworkStartsApi
import com.markettwits.sportsouce.starts.common.data.mapper.StartsCloudToListMapper
import com.markettwits.sportsouce.starts.common.domain.SportSauceStartsApi
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

internal class SportSauceStartsApiBase(
    private val startsCloudApi: SportSauceNetworkStartsApi,
    private val mapper: StartsCloudToListMapper
) : SportSauceStartsApi {
    override suspend fun startWithFilter(request: Map<String, String>): List<StartsListItem> =
        mapper.mapSingle(startsCloudApi.startWithFilter(request).rows)


    override suspend fun fetchActualStarts(): List<StartsListItem> =
        mapper.mapSingle(startsCloudApi.fetchActualStarts().rows)

    override suspend fun fetchPasteStarts(): List<StartsListItem> =
        mapper.mapSingle(startsCloudApi.fetchPasteStarts().rows)


    override suspend fun fetchPreview(): List<StartsListItem> =
        mapper.mapSingle(startsCloudApi.fetchPreview().rows)

    override suspend fun fetchStartMain(): List<StartsListItem> =
        mapper.mapSingle(startsCloudApi.fetchStartMain().rows)
}