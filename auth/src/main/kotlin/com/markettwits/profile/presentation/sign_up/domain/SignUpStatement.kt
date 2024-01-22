package com.markettwits.profile.presentation.sign_up.domain

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