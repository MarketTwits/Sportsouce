package com.markettwits.club.cloud.models.questions

import kotlinx.serialization.Serializable

@Serializable
data class QuestionRemoteRow(
    val count: Int,
    val rows: List<QuestionRemote>
)