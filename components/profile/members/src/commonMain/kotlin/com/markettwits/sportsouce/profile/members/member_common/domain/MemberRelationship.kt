package com.markettwits.sportsouce.profile.members.member_common.domain

object MemberRelationship {
    val options = listOf("Родственник", "Друг", "Партнер по команде", "Тренер", "Ученик")
    fun optionsFor(current: String): List<String> =
        if (current.isBlank() || current in options) options else options + current
}
