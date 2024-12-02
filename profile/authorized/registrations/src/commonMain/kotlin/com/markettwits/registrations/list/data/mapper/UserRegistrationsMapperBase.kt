package com.markettwits.registrations.list.data.mapper

import com.markettwits.cloud.model.common.StartStatus
import com.markettwits.cloud.model.start_user.values.RemoteGroup
import com.markettwits.cloud.model.start_user.values.UserRegistrationOld
import com.markettwits.cloud.model.start_user.values.UserRegistrationNew
import com.markettwits.cloud.model.start_user.values.UserRegistrationShared
import com.markettwits.core_ui.items.base_extensions.formatPrice
import com.markettwits.registrations.list.domain.StartOrderInfo
import com.markettwits.time.TimeMapper
import com.markettwits.time.TimePattern
import kotlinx.serialization.json.Json
import java.text.NumberFormat
import java.util.Locale

class UserRegistrationsMapperBase(private val timeMapper: TimeMapper) : UserRegistrationsMapper {

    override fun map(start: UserRegistrationNew): StartOrderInfo {
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
            statusCode = StartStatus(
                start.start.start_status?.code ?: 3,
                start.start.start_status?.name ?: "Без статуса"
            ),
            ageGroup = start.members.first().group ?: "",
            distance = start.members.first().distance ?: "",
            member = start.members.first().surname + " " + start.members.first().name,
            cost = if (start.price == null) "0" else start.price?.formatPrice().toString(),
            team = start.members.first().team,
            kindOfSport = start.members.first().format ?: "",
            startTitle = start.start.name,
            payment = StartOrderInfo.PaymentStatus.NotPaid(),
        )
    }

    override fun map(
        start: UserRegistrationOld
    ): StartOrderInfo {
            return StartOrderInfo(
                id = start.id,
                startId = start.start_id,
                name = start.name,
                image = start.start.posterLinkFile?.fullPath ?: "",
                dateStartPreview = timeMapper.mapTime(
                    TimePattern.FullWithEmptySpace,
                    start.start.start_date
                ),
                dateStartCloud = start.start.start_date,
                statusCode = StartStatus(
                    start.start.start_status?.code ?: 3,
                    start.start.start_status?.name ?: "Без статуса"
                ),
                ageGroup = mapStartGroup(start.group ?: ""),
                distance = start.distance,
                member = start.surname + " " + start.name,
                cost = if (start.price == null) "0" else start.price.toString(),
                team = start.team,
                kindOfSport = start.format,
                startTitle = start.start.name,
                payment = mapPayments(
                    start.payment,
                    start.start.isOpen ?: false,
                    start.start.payment_disabled ?: false
                ),
            )
    }

    override fun map(registrations: List<UserRegistrationShared>): List<StartOrderInfo> {
        return registrations.map {
            when(it){
                is UserRegistrationNew -> map(it)
                is UserRegistrationOld -> map(it)
            }
        }
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
            return StartOrderInfo.PaymentStatus.NotPaid(
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

        else return StartOrderInfo.PaymentStatus.NotPaid(
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