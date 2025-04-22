package com.markettwits.sportsouce.profile.registrations.domain

import kotlinx.serialization.Serializable

/**
 * Описание различных состояний оплаты:
 * - Старт оплачен - payment: 1
 * - Участие бесплатно - payment: 2
 * - Старт не оплачен - payment : 3
 * - Участие бесплатно по скидке - payment : 4
 * - Старт не оплачен - payment null
 * - Оплата отменена - payment 0
 */
@Serializable
sealed interface StartOrderPaymentStatus {

    val isPaid: Boolean

    val title: String

    @Serializable
    data class Success(
        override val isPaid: Boolean = true,
        override val title: String = "Оплачено"
    ) : StartOrderPaymentStatus

    @Serializable
    data class Free(
        override val isPaid: Boolean = true,
        override val title: String = "Бесплатно"
    ) : StartOrderPaymentStatus

    @Serializable
    data class OnPlace(
        override val isPaid: Boolean = true,
        override val title: String = "На месте старта"
    ) : StartOrderPaymentStatus

    @Serializable
    data class NotPaid(
        override val isPaid: Boolean = false,
        override val title: String = "Не оплачено"
    ) : StartOrderPaymentStatus

    @Serializable
    data class PaymentCancelled(
        override val isPaid: Boolean = false,
        override val title: String = "Оплата отменена"
    ) : StartOrderPaymentStatus

    @Serializable
    data class WithoutStatus(
        override val isPaid: Boolean = true,
        override val title: String = "Без статуса"
    ) : StartOrderPaymentStatus
}