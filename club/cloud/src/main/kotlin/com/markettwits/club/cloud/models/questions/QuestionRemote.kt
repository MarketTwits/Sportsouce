package com.markettwits.club.cloud.models.questions

import kotlinx.serialization.Serializable

@Serializable
internal data class QuestionRemote(
    val count: Int,
    val rows: List<QuestionRemoteRow>
)