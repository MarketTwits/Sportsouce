package com.markettwits.registrations.registrations.data

import com.markettwits.cloud.model.common.StartStatus
import com.markettwits.cloud.model.start_user.RemoteGroup
import com.markettwits.cloud.model.start_user.RemouteStartsUserItem
import com.markettwits.core_ui.time.TimeMapper
import com.markettwits.core_ui.time.TimePattern
import com.markettwits.registrations.registrations.presentation.RegistrationsStore
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
                    payment = mapPayment(it.payment, it.start.isOpen, it.start.payment_disabled ?: false)
                )
            }
            return RegistrationsStore.State(
                info = base,
                paymentState = mapPaymentList(base),
            )
        }

        private fun mapPayment(payment: Int?, isOpen: Boolean, paymentDisabled : Boolean): Boolean {
            return !(payment == null && isOpen && !paymentDisabled)
        }

        private fun mapPaymentList(base: List<RegistrationsStore.StartsStateInfo>): RegistrationsStore.StartPaymentState {
            val list = base
                .filter { !it.payment }
            val count = list.size
            val cost = list.sumOf { it.cost.toDoubleOrNull() ?: 0.0 }
            return RegistrationsStore.StartPaymentState(
                paymentList = list,
                count = count,
                totalCost = cost.toInt()
            )
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