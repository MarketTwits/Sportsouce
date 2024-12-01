package com.markettwits.start_cloud.model.register.price.fields

import kotlinx.serialization.Serializable

@Serializable
data class StartRegisterMember(
    val answers: List<StartRegisterAnswer>,
    val birthday: String,
    val city: String,
    val email: String,
    val gender: String,
    val name: String,
    val phone: String,
    val stage_id: Int?,
    val surname: String,
    val team: String,
    val user_id: String
)