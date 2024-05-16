package com.markettwits.club.info.domain.models

import kotlinx.serialization.Serializable

@Serializable
class Question(
    val answer: String,
    val id: Int,
    val question: String
)