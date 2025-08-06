package com.markettwits.sportsouce.auth.flow.internal.sign_in.domain

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null,
)