package com.markettwits.registrations.registrations_list.domain

import com.markettwits.cloud.model.common.StartStatus
import kotlinx.serialization.Serializable

@Serializable
data class StartOrderInfo(
    val id: Int,
    val startId: Int,
    val name: String,
    val image: String,
    val dateStartPreview: String,
    val dateStartCloud: String,
    val statusCode: StartStatus,
    val team: String,
    val payment: PaymentStatus,
    val ageGroup: String,
    val distance: String,
    val member: String,
    val kindOfSport: String,
    val startTitle: String,
    val cost: String,
) {

    /**
     * Описание различных состояний оплаты:
     * - Старт оплачен - payment: 1
     * - Участие бесплатно - payment: 2
     * - Старт не оплачен - payment null
     * - Оплата отменена - payment 0
     */
    @Serializable
    sealed interface PaymentStatus {
        val payment: Boolean
        val title: String

        @Serializable
        data class Success(
            override val payment: Boolean = true,
            override val title: String = "Оплачено"
        ) : PaymentStatus

        @Serializable
        data class Free(
            override val payment: Boolean = true,
            override val title: String = "Бесплатно"
        ) : PaymentStatus

        @Serializable
        data class Failure(
            override val payment: Boolean = false,
            override val title: String = "Не оплачено"
        ) : PaymentStatus

        @Serializable
        data class PaymentCancelled(
            override val payment: Boolean = false,
            override val title: String = "Оплата отменена"
        ) : PaymentStatus
    }
}