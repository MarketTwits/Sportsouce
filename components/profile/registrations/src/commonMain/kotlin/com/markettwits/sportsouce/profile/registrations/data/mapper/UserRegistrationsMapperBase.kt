package com.markettwits.sportsouce.profile.registrations.data.mapper

import com.markettwits.core.time.TimeMapper
import com.markettwits.core.time.TimePattern
import com.markettwits.core_ui.items.extensions.formatPrice
import com.markettwits.sportsouce.profile.cloud.model.registrations.Group
import com.markettwits.sportsouce.profile.cloud.model.registrations.MemberResult
import com.markettwits.sportsouce.profile.cloud.model.registrations.UserRegistration
import com.markettwits.sportsouce.profile.cloud.model.start_price.StartPriceResponse
import com.markettwits.sportsouce.profile.registrations.domain.*
import kotlinx.serialization.json.Json
import kotlin.random.Random

class UserRegistrationsMapperBase(
    private val timeMapper: TimeMapper
) : UserRegistrationsMapper {

    override fun map(start: UserRegistration): StartOrderInfo {

        return StartOrderInfo(
            id = start.id ?: Random.nextInt(),
            startId = start.startId ?: Random.nextInt(),
            name = start.start?.name ?: "Без названия",
            image = start.start?.posterLinkFile?.fullPath ?: "",
            dateStartPreview = timeMapper.mapTime(
                TimePattern.FullWithEmptySpace,
                start.start?.startDate ?: ""
            ),
            dateStartCloud = start.start?.startDate ?: "",
            members = start.members?.map { member ->
                val ageGroup = member.ageGroup?.name ?: mapStartGroup(member.group)
                val distance = member.distanceRelation?.name ?: member.distance ?: ""
                val format = member.distanceRelation?.format ?: member.format ?: ""
                StartOrderMember(
                    name = member.name ?: "Без имени",
                    surname = member.surname ?: "Без фамилии",
                    teamName = member.team ?: "N/A",
                    ageGroupName = ageGroup,
                    distanceName = distance,
                    genderName = member.gender ?: "N/A",
                    formatName = format,
                    results = member.results?.map { mapMemberResult(it) } ?: emptyList()
                )
            } ?: emptyList(),
            cost = start.price.formatPrice(),
            costWithoutDiscount = start.priceWithoutDiscount.formatPrice(),
            additionalFieldsCost = start.priceOfAdditionalFields.formatPrice(),
            startTitle = start.start?.name ?: "",
            promo = start.promocode?.code ?: "",
            payment = mapPayments(
                payment = start.payment,
                successPaymentReason = start.successPaymentReason
            ),
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
        successPaymentReason: String?,
    ): StartOrderPaymentStatus {
        val reason = successPaymentReason ?: ""
        return when (payment) {
            0 -> StartOrderPaymentStatus.NotPaid(isPaid = false)
            1 -> StartOrderPaymentStatus.Success(paymentReason = reason)
            2 -> StartOrderPaymentStatus.OnPlace(paymentReason = reason)
            4 -> StartOrderPaymentStatus.Free(paymentReason = reason)
            3 -> StartOrderPaymentStatus.NotPaid(isPaid = false)
            else -> StartOrderPaymentStatus.NotPaid()
        }
    }

    private fun mapMemberResult(memberResult: MemberResult): StartOrderMemberResult {
        return StartOrderMemberResult(
            bodyNumber = memberResult.bodyNumber ?: "",
            circles = memberResult.circles ?: emptyMap(),
            distance = memberResult.distance ?: "",
            id = memberResult.id,
            memberStartId = memberResult.memberStartId,
            userName = memberResult.name ?: "",
            place = memberResult.place ?: 999,
            result = memberResult.result ?: "",
            sex = memberResult.sex ?: "",
            shift = memberResult.shift ?: "",
            team = memberResult.team ?: "",
        )
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