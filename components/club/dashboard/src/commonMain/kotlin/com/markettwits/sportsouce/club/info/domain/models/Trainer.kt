package com.markettwits.sportsouce.club.info.domain.models

import kotlinx.serialization.Serializable

@Serializable

class Trainer(
    val id: Int,
    val name: String,
    val surname: String,
    val description: String,
    val imageUrl: String,
    val kindOfSports: List<String>
) {
    fun fullName(): String = "$name $surname"

    fun sports(): String = kindOfSports.joinToString(separator = " ") { "#$it" }
}
