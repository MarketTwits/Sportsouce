package com.markettwits.sportsouce.club.cloud.models.questions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class QuestionRemote(
    @SerialName("count")
    val count: Int,
    @SerialName("rows")
    val rows: List<QuestionRemoteRow>
)