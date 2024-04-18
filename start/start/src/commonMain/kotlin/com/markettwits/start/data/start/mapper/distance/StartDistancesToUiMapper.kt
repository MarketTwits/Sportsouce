package com.markettwits.start.data.start.mapper.distance

import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.cloud.model.start_member.StartMemberItem

internal interface StartDistancesToUiMapper {
    fun mapDistanceInfoList(
        startMember: List<StartMemberItem>,
        distances: List<DistanceItem>,
        date: String
    ): List<DistanceItem>

    fun mapKindOfSportsToDistanceItemList(
        startId: Int,
        kindOfSport: String
    ): List<DistanceItem>


}