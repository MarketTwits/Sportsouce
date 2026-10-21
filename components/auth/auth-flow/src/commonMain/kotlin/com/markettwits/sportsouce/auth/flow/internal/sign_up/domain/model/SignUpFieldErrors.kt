package com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.model

/** Errors belong to the field, so returning to a step preserves its validation state. */
data class SignUpFieldErrors(
    val phone: String? = null,
    val email: String? = null,
    val name: String? = null,
    val surname: String? = null,
    val sex: String? = null,
    val birthday: String? = null,
    val password: String? = null,
    val repeatPassword: String? = null,
) {
    fun firstInvalidStage(): SignUpStage? = when {
        phone != null || email != null -> SignUpStage.FIRST
        name != null || surname != null || sex != null || birthday != null -> SignUpStage.SECOND
        password != null || repeatPassword != null -> SignUpStage.THIRD
        else -> null
    }

    fun firstMessage(): String? = phone ?: email ?: name ?: surname ?: sex ?: birthday ?: password ?: repeatPassword
}
