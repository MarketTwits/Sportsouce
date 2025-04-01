package com.markettwits.sportsouce.start.cloud.model.register.price.fields

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartRegisterMember(
    @SerialName("answers")
    val answers: List<StartRegisterAnswer>,
    @SerialName("birthday")
    val birthday: String,
    @SerialName("city")
    val city: String,
    @SerialName("email")
    val email: String,
    @SerialName("gender")
    val gender: String,
    @SerialName("name")
    val name: String,
    @SerialName("phone")
    val phone: String,
    @SerialName("stage_id")
    val stageId: Int?,
    @SerialName("surname")
    val surname: String,
    @SerialName("team")
    val team: String,
    @SerialName("user_id")
    val userId: String
)