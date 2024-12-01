package com.markettwits.start.data.start.mapper.distance

import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.cloud.model.start.Discount
import com.markettwits.cloud.model.start_member.StartMemberItem
import com.markettwits.start_cloud.model.members.StartMember

internal interface StartDistancesToUiMapper {

    fun mapDistanceInfoList(
        startMember: List<StartMember>,
        distances: List<DistanceItem>,
        date: String
    ): List<DistanceItem>

    fun mapKindOfSportsToDistanceItemList(
        startId: Int,
        kindOfSport: String
    ): List<DistanceItem>

    fun mapDiscountCloud(discount: List<com.markettwits.start_cloud.model.start.fields.Discount>): List<DistanceItem.Discount>

}