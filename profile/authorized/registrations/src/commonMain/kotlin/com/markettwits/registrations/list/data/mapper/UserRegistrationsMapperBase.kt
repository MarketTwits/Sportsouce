package com.markettwits.registrations.list.data.mapper

import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.cloud.model.start_user.values.UserRegistration
import com.markettwits.core_ui.items.base_extensions.formatPrice
import com.markettwits.registrations.list.domain.StartOrderInfo
import com.markettwits.time.TimeMapper
import com.markettwits.time.TimePattern
import kotlinx.serialization.json.Json

class UserRegistrationsMapperBase(
    private val timeMapper: TimeMapper
) : UserRegistrationsMapper {

    override fun map(start: UserRegistration): StartOrderInfo {

        val member = start.members.first()

        val ageGroup = member.age_group?.name ?: mapStartGroup(member.group)

        val distance = member.distance_relation?.name ?: member.distance ?: ""

        val format = member.distance_relation?.format ?: member.format ?: ""

        return StartOrderInfo(
            id = start.id,
            startId = start.start_id,
            name = start.start.name,
            image = start.start.posterLinkFile?.fullPath ?: "",
            dateStartPreview = timeMapper.mapTime(
                TimePattern.FullWithEmptySpace,
                start.start.start_date
            ),
            dateStartCloud = start.start.start_date,
            ageGroup = ageGroup,
            distance = distance,
            member = member.surname + " " + member.name,
            cost = if (start.price == null) "0" else start.price?.formatPrice().toString(),
            team = member.team,
            kindOfSport = format,
            startTitle = start.start.name,
            payment = mapPayments(payment = start.payment),
        )
    }

    override fun map(registrations: List<UserRegistration>): List<StartOrderInfo> {
        return registrations.map {
            map(it)
        }
    }

    private fun mapPayments(
        payment: Int?,
    ): StartOrderInfo.PaymentStatus {
        return when(payment){
            0 -> StartOrderInfo.PaymentStatus.NotPaid()
            1 -> StartOrderInfo.PaymentStatus.Success()
            2 -> StartOrderInfo.PaymentStatus.Free()
            4 -> StartOrderInfo.PaymentStatus.Free()
            else -> StartOrderInfo.PaymentStatus.NotPaid()
        }
    }

    private fun mapStartGroup(group: String?): String {
        val json = Json {
            ignoreUnknownKeys = true
        }
        return try {
            json.decodeFromString<DistanceItem.Group>(group ?: "").name
        } catch (e: Exception) {
            ""
        }
    }
}