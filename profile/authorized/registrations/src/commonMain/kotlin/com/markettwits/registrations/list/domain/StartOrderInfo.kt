package com.markettwits.registrations.list.domain

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
    val payment: PaymentStatus,
    val members: List<Member>,
    val startTitle: String,
    val cost: String,
) {

    @Serializable
    data class Member(
        val name : String,
        val surname : String,
        val teamName : String,
        val ageGroupName: String,
        val distanceName : String,
        val genderName : String,
        val formatName : String,
    )

    /**
     * Описание различных состояний оплаты:
     * - Старт оплачен - payment: 1
     * - Участие бесплатно - payment: 2
     * - Участие бесплатно по скидке - payment 4
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
        data class NotPaid(
            override val payment: Boolean = false,
            override val title: String = "Не оплачено"
        ) : PaymentStatus

        @Serializable
        data class PaymentCancelled(
            override val payment: Boolean = false,
            override val title: String = "Оплата отменена"
        ) : PaymentStatus

        @Serializable
        data class WithoutStatus(
            override val payment: Boolean = true,
            override val title: String = "Без статуса"
        ) : PaymentStatus
    }
}

fun List<StartOrderInfo>.containsUnpaymant(): Boolean {
    return this.any { !it.payment.payment }
}

fun List<StartOrderInfo>.indexOfFirstUnpaid(): Int {
    return this.indexOfFirst { !it.payment.payment }
}

fun List<StartOrderInfo>.firstUnpaidItem(): StartOrderInfo? {
    return this.firstOrNull { !it.payment.payment }
}