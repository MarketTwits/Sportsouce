package com.markettwits.sportsouce.start.support.domain

data class StartDonationCheck(
    val risk: String,
    val registration: Registration?,
    val matchedAmountKopecks: Long?,
) {
    val hasUnpaidRegistration: Boolean
        get() = risk == "registered_unpaid" || registration?.paymentStatus == 0
    val requiresConfirmation: Boolean
        get() = risk != "none" || hasUnpaidRegistration

    data class Registration(val groupId: Int, val paymentStatus: Int, val amountKopecks: Long?, val canPay: Boolean)
}
