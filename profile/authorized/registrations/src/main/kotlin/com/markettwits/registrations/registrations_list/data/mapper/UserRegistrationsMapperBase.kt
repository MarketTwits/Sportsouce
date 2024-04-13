package com.markettwits.registrations.registrations_list.data.mapper

import com.markettwits.cloud.model.common.StartStatus
import com.markettwits.cloud.model.start_user.RemoteGroup
import com.markettwits.cloud.model.start_user.RemoteStartsUserItem
import com.markettwits.registrations.registrations_list.domain.StartOrderInfo
import com.markettwits.time.TimeMapper
import com.markettwits.time.TimePattern
import kotlinx.serialization.json.Json

class UserRegistrationsMapperBase(private val timeMapper: TimeMapper) : UserRegistrationsMapper {
    override fun map(starts: List<RemoteStartsUserItem>): List<StartOrderInfo> {
        val base = starts.map {
            StartOrderInfo(
                id = it.id,
                startId = it.start_id,
                name = it.name,
                image = it.start.posterLinkFile?.fullPath ?: "",
                dateStartPreview = timeMapper.mapTime(TimePattern.ddMMMMyyyy, it.start.start_date),
                dateStartCloud = it.start.start_date,
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
                payment = mapPayments(
                    it.payment,
                    it.start.isOpen,
                    it.start.payment_disabled ?: false
                ),
            )
        }
        return base
    }

    private fun mapPayment(payment: Int?, isOpen: Boolean, paymentDisabled: Boolean): Boolean {
        val pay = payment == null || payment == 0
        return !(pay && isOpen && !paymentDisabled)
    }

    private fun mapPayments(
        payment: Int?,
        isOpen: Boolean,
        paymentDisabled: Boolean
    ): StartOrderInfo.PaymentStatus {
        if (paymentDisabled || payment == 2)
            return StartOrderInfo.PaymentStatus.Free()
        if (payment == null)
            return StartOrderInfo.PaymentStatus.Failure(
                mapPayment(
                    payment,
                    isOpen,
                    paymentDisabled
                )
            )
        if (payment == 0)
            return StartOrderInfo.PaymentStatus.PaymentCancelled(
                mapPayment(
                    payment,
                    isOpen,
                    paymentDisabled
                )
            )
        if (payment == 1)
            return StartOrderInfo.PaymentStatus.Success()
        else return StartOrderInfo.PaymentStatus.Failure(
            mapPayment(
                payment,
                isOpen,
                paymentDisabled
            )
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