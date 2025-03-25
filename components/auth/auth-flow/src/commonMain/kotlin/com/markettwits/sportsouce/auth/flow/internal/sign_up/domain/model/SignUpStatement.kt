package com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.model

data class SignUpStatement(
    val name: String = "",
    val surname: String = "",
    val sex: String = "",
    val birthday: String = "",
    val phone: String = "",
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = ""
)