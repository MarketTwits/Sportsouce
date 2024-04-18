package com.markettwits.start.register.domain

data class StartRegistryResult(
    val isSuccess: Boolean,
    val isError: Boolean,
    val message: String,
    val paymentUrl: String
)