package com.markettwits.registrations.data

import com.markettwits.cloud.model.start_user.RemoteGroup
import com.markettwits.cloud.model.start_user.RemouteStartsUserItem
import com.markettwits.core_ui.time.TimeMapper
import com.markettwits.core_ui.time.TimePattern
import com.markettwits.registrations.presentation.RegistrationsStore
import com.markettwits.starts.StartsListItem
import kotlinx.serialization.json.Json

interface RemoteRegistrationsToUiMapper {
    fun map(cloud: List<RemouteStartsUserItem>): RegistrationsStore.State

    class Base(
        private val timeMapper: TimeMapper
    ) : RemoteRegistrationsToUiMapper {
        override fun map(cloud: List<RemouteStartsUserItem>): RegistrationsStore.State {
            val base = cloud.map {
                RegistrationsStore.StartsStateInfo(
                    id = it.id,
                    name = it.name,
                    image = it.start.posterLinkFile?.fullPath ?: "",
                    dateStart = timeMapper.mapTime(TimePattern.ddMMMMyyyy, it.start.start_date),
                    statusCode = StartsListItem.StatusCode(
                        it.start.start_status.code,
                        it.start.start_status.name
                    ),
                    ageGroup = mapStartGroup(it.group),
                    distance = it.distance,
                    member = it.surname + " " + it.name,
                    cost = it.price.toString(),
                    team = it.team,
                    kindOfSport = it.format,
                    startTitle = it.start.name,
                    payment = mapPayment(it.payment, it.start.isOpen)
                )
            }
            return RegistrationsStore.State(
                info = base,
                paymentList = mapPaymentList(base)
            )
        }

        private fun mapPayment(payment: Int?, isOpen: Boolean): Boolean {
            return !(payment == null && isOpen)
        }

        private fun mapPaymentList(base: List<RegistrationsStore.StartsStateInfo>): List<RegistrationsStore.StartsStateInfo> {
            return base.filter { !it.payment }
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
}