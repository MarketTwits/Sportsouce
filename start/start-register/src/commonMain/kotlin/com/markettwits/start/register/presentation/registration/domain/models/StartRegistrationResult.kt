package com.markettwits.start.register.presentation.registration.domain.models

data class StartRegistrationResult(
    val isSuccess: Boolean,
    val isError: Boolean,
    val paymentUrl: String
)