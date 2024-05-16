package com.markettwits.club.cloud.models.questions

import kotlinx.serialization.Serializable

@Serializable
data class QuestionRemote(
    val answer: String,
    val id: Int,
    val question: String
)
