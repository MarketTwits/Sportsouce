package com.markettwits.sportsouce.profile.registrations.list.data.mapper

import com.markettwits.core.time.TimeMapper
import com.markettwits.core.time.TimePattern
import com.markettwits.core_ui.items.extensions.formatPrice
import com.markettwits.sportsouce.profile.cloud.model.registrations.Group
import com.markettwits.sportsouce.profile.cloud.model.registrations.UserRegistration
import com.markettwits.sportsouce.profile.cloud.model.start_price.StartPriceResponse
import com.markettwits.sportsouce.profile.registrations.list.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.list.domain.StartOrderPrice
import kotlinx.serialization.json.Json

class UserRegistrationsMapperBase(
    private val timeMapper: TimeMapper
) : UserRegistrationsMapper {

    override fun map(start: UserRegistration): StartOrderInfo {

        return StartOrderInfo(
            id = start.id,
            startId = start.startId,
            name = start.start.name,
            image = start.start.posterLinkFile?.fullPath ?: "",
            dateStartPreview = timeMapper.mapTime(
                TimePattern.FullWithEmptySpace,
                start.start.startDate
            ),
            dateStartCloud = start.start.startDate,
            members = start.members.map { member ->
                val ageGroup = member.ageGroup?.name ?: mapStartGroup(member.group)
                val distance = member.distanceRelation?.name ?: member.distance ?: ""
                val format = member.distanceRelation?.format ?: member.format ?: ""
                StartOrderInfo.Member(
                    name = member.name,
                    surname = member.surname,
                    teamName = member.team,
                    ageGroupName = ageGroup,
                    distanceName = distance,
                    genderName = member.gender,
                    formatName = format
                )
            },
            cost = start.price.formatPrice(),
            startTitle = start.start.name,
            payment = mapPayments(payment = start.payment),
        )
    }

    override fun map(registrations: List<UserRegistration>): List<StartOrderInfo> {
        return registrations.map {
            map(it)
        }
    }

    override fun mapPrice(priceResponse: StartPriceResponse): StartOrderPrice =
        StartOrderPrice(
            additionalPrice = priceResponse.additionalFieldsPrice.formatPrice(),
            totalPrice = priceResponse.totalPrice.formatPrice(),
            isRequired = priceResponse.isPaymentRequired
        )

    private fun mapPayments(
        payment: Int?,
    ): StartOrderInfo.PaymentStatus {
        return when(payment){
            0 -> StartOrderInfo.PaymentStatus.NotPaid()
            1 -> StartOrderInfo.PaymentStatus.Success()
            2 -> StartOrderInfo.PaymentStatus.OnPlace()
            4 -> StartOrderInfo.PaymentStatus.Free()
            else -> StartOrderInfo.PaymentStatus.NotPaid()
        }
    }

    private fun mapStartGroup(group: String?): String {
        val json = Json {
            ignoreUnknownKeys = true
        }
        return try {
            json.decodeFromString<Group>(group ?: "").name
        } catch (e: Exception) {
            ""
        }
    }
}