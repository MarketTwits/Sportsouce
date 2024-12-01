package com.markettwits.start.data.start.mapper.distance

import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.cloud.model.start.Discount
import com.markettwits.cloud.model.start_member.StartMemberItem
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.start_cloud.model.members.StartMember
import kotlinx.serialization.json.Json

internal class StartDistancesToUiMapperBase(
    private val exceptionTracker: ExceptionTracker
) : StartDistancesToUiMapperAbstract() {
    override fun mapDistanceInfoList(
        startMember: List<StartMember>,
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

    override fun mapDiscountCloud(discount: List<com.markettwits.start_cloud.model.start.fields.Discount>): List<DistanceItem.Discount> =
        discount.map {
            DistanceItem.Discount(
                id = it.id,
                start_id = it.start_id,
                c_from = it.c_from,
                c_to = it.c_to,
                value = it.value,
                percent = it.percent ?: true,
            )
        }

    private fun mapKindOfSportsToDistanceItemListBase(
        startId: Int,
        kindOfSport: String
    ): List<DistanceItem> = try {
        json.decodeFromString<List<DistanceItem>>(kindOfSport)
    } catch (e: Exception) {
        exceptionTracker.setKey(Pair("startId", startId.toString()))
        exceptionTracker.setLog(kindOfSport)
        exceptionTracker.reportException(e, "KindOfSportsToDistanceItemJson")
        emptyList()
    }
}
