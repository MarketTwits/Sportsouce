package com.markettwits.registrations.registrations.data.mapper

import com.markettwits.cloud.model.common.StartStatus
import com.markettwits.cloud.model.start_user.RemoteGroup
import com.markettwits.cloud.model.start_user.RemoteStartsUserItem
import com.markettwits.core_ui.time.TimeMapper
import com.markettwits.core_ui.time.TimePattern
import com.markettwits.registrations.registrations.domain.StartsStateInfo
import kotlinx.serialization.json.Json

class UserRegistrationsMapperBase(private val timeMapper: TimeMapper) : UserRegistrationsMapper {
    override fun map(starts: List<RemoteStartsUserItem>): List<StartsStateInfo> {
        val base = starts.map {
            StartsStateInfo(
                id = it.id,
                startId = it.start_id,
                name = it.name,
                image = it.start.posterLinkFile?.fullPath ?: "",
                dateStart = timeMapper.mapTime(TimePattern.ddMMMMyyyy, it.start.start_date),
                statusCode = StartStatus(
                    it.start.start_status.code,
                    it.start.start_status.name
                ),
                ageGroup = mapStartGroup(it.group ?: ""),
                distance = it.distance,
                member = it.surname + " " + it.name,
                cost = it.price.toString(),
                team = it.team,
                kindOfSport = it.format,
                startTitle = it.start.name,
                payment = mapPayment(
                    it.payment,
                    it.start.isOpen,
                    it.start.payment_disabled ?: false
                )
            )
        }
        return base
    }

    private fun mapPayment(payment: Int?, isOpen: Boolean, paymentDisabled: Boolean): Boolean {
        return !(payment == null && isOpen && !paymentDisabled)
    }

    private fun mapStartGroup(group: String): String {
        val json = Json {
            ignoreUnknownKeys = true
        }
        return try {
            val group = json.decodeFromString<RemoteGroup>(group)
            group.name
        } catch (e: Exception) {
            ""
        }
    }
}