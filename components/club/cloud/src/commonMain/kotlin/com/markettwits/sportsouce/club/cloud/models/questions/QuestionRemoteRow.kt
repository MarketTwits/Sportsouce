package com.markettwits.sportsouce.club.cloud.models.questions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionRemoteRow(
    @SerialName("answer")
    val answer: String,
    @SerialName("id")
    val id: Int,
    @SerialName("question")
    val question: String
)
