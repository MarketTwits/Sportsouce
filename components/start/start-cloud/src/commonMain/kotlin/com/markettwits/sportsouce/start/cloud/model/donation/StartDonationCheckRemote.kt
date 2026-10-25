package com.markettwits.sportsouce.start.cloud.model.donation

import kotlinx.serialization.Serializable

@Serializable
data class StartDonationCheckRemote(
    val risk: String,
    val registration: Registration? = null,
    val matchedAmountKopecks: Long? = null,
) {
    @Serializable
    data class Registration(
        val groupId: Int,
        val paymentStatus: Int,
        val amountKopecks: Long? = null,
        val canPay: Boolean = false,
    )
}
