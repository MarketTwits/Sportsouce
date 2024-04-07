package com.markettwits.start.data.start.mapper.distance

import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.cloud.model.start_member.StartMemberItem
import com.markettwits.inappnotification.api.tracker.AnalyticsTracker
import kotlinx.serialization.json.Json

internal class StartDistancesToUiMapperBase(
    private val analyticsTracker: AnalyticsTracker
) : StartDistancesToUiMapperAbstract() {
    override fun mapDistanceInfoList(
        startMember: List<StartMemberItem>,
        distances: List<DistanceItem>,
        date: String
    ): List<DistanceItem> = distances.map { distanceInfo ->
        when (distanceInfo) {
            is DistanceItem.DistanceInfo -> distanceInfo.copy(
                distance = mapDistanceInfo(
                    startMember = startMember,
                    distanceInfo = distanceInfo,
                    date = mapCurrentTime(date)
                ),
            )

            is DistanceItem.DistanceCombo -> distanceInfo.copy(
                distances = distanceInfo.distances,
                price = mapPriceForDistanceCombo(
                    sale = distanceInfo.sale?.toInt() ?: 0,
                    distances = distanceInfo.distances,
                    date = mapCurrentTime(date),
                ),
                value = mapComboTitle(
                    comboTitle = distanceInfo.value,
                    distances = distanceInfo.distances
                )
            )
        }
    }

    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

    override fun mapKindOfSportsToDistanceItemList(
        startId: Int,
        kindOfSport: String
    ): List<DistanceItem> = mapKindOfSportsToDistanceItemListBase(startId, kindOfSport)

    fun mapKindOfSportsToDistanceItemListBase(
        startId: Int,
        kindOfSport: String
    ): List<DistanceItem> = try {
        json.decodeFromString<List<DistanceItem>>(kindOfSport)
    } catch (e: Exception) {
        analyticsTracker.setKey(Pair("startId", startId.toString()))
        analyticsTracker.setLog(kindOfSport)
        analyticsTracker.reportException(e, "KindOfSportsToDistanceItemJson")
        emptyList()
    }
}
