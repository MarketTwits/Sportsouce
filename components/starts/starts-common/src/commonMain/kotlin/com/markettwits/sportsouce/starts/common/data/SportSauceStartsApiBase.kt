package com.markettwits.sportsouce.starts.common.data

import com.markettwits.sportsouce.starts.cloud.SportSauceNetworkStartsApi
import com.markettwits.sportsouce.starts.cloud.model.StartStatusEntity
import com.markettwits.sportsouce.starts.common.data.mapper.StartsCloudToListMapper
import com.markettwits.sportsouce.starts.common.domain.SportSauceStartsApi
import com.markettwits.sportsouce.starts.common.domain.StartStatus
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

internal class SportSauceStartsApiBase(
    private val startsCloudApi: SportSauceNetworkStartsApi,
    private val mapper: StartsCloudToListMapper,
) : SportSauceStartsApi {
    override suspend fun startWithFilter(request: Map<String, String>): List<StartsListItem> =
        mapper.mapSingle(startsCloudApi.startWithFilter(request).rows)

    override suspend fun fetchStarts(
        limit: Int?,
        offset: Int?,
        isMain: Boolean?,
        openFirst: Boolean?,
        isGroup: Boolean?,
        statuses: List<StartStatus>,
        additionalParameters: Map<String, String>,
    ): List<StartsListItem> = mapper.mapSingle(
        startsCloudApi.fetchStarts(
            limit = limit,
            offset = offset,
            isMain = isMain,
            openFirst = openFirst,
            isGroup = isGroup,
            statuses = statuses.map { it.toCloud() },
            additionalParameters = additionalParameters,
        ).rows
    )

    override suspend fun fetchActualStarts(): List<StartsListItem> =
        mapper.mapSingle(startsCloudApi.fetchActualStarts().rows)

    override suspend fun fetchPasteStarts(): List<StartsListItem> =
        mapper.mapSingle(startsCloudApi.fetchPasteStarts().rows)


    override suspend fun fetchPreviewStarts(): List<StartsListItem> =
        mapper.mapSingle(startsCloudApi.fetchPreview().rows)

    override suspend fun fetchRelatedStarts(seriesId: Int): List<StartsListItem> =
        mapper.mapSingle(startsCloudApi.fetchSeries(seriesId).rows)

    override suspend fun fetchStartMain(): List<StartsListItem> =
        mapper.mapSingle(startsCloudApi.fetchStartMain().rows)

    override suspend fun fetchFavoriteStarts(userId: Int, token: String): List<StartsListItem> {
        val request = startsCloudApi.fetchFavorites(userId = userId.toString(), token = token)
        return mapper.mapSingle(request.rows)
    }

    override suspend fun addToFavorite(userId: Int, token: String, startId: Int) {
        startsCloudApi.addFavorites(startId = startId, token = token, userId = userId.toString())
    }

    override suspend fun removeFromFavorite(userId: Int, token: String, startId: Int) {
        startsCloudApi.removeFavorites(startId = startId, token = token, userId = userId.toString())
    }

    private fun StartStatus.toCloud(): StartStatusEntity = when (this) {
        StartStatus.PENDING -> StartStatusEntity.PENDING
        StartStatus.ANNOUNCEMENT -> StartStatusEntity.ANNOUNCEMENT
        StartStatus.REGISTRATION_OPEN -> StartStatusEntity.REGISTRATION_OPEN
        StartStatus.WAITING -> StartStatusEntity.WAITING
        StartStatus.IS_PASSING -> StartStatusEntity.IS_PASSING
        StartStatus.ENDED -> StartStatusEntity.ENDED
    }
}
