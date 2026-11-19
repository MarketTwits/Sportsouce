package com.markettwits.sportsouce.start.support.domain

data class StartDonationCheck(
    val risk: String,
    val registration: Registration?,
    val matchedAmountKopecks: Long?,
    val hasConfirmedRegistration: Boolean = false,
    val isRegistrationKnown: Boolean = true,
) {
    val hasUnpaidRegistration: Boolean
        get() = when (registration?.paymentStatus) {
            0, 3 -> true
            else -> risk == "registered_unpaid"
        }
    val requiresConfirmation: Boolean
        get() = risk != "none" || hasUnpaidRegistration
    val hasRegistration: Boolean
        get() = registration != null || hasUnpaidRegistration || hasConfirmedRegistration

    data class Registration(val groupId: Int, val paymentStatus: Int, val amountKopecks: Long?, val canPay: Boolean)
}

data class CachedStartRegistration(val accountId: Int, val check: StartDonationCheck?)
