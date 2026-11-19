package com.markettwits.sportsouce.unauthorized.domain

import kotlinx.serialization.Serializable

/** Remote data controls content, while layout, colors and actions remain in the design system. */
@Serializable
data class GuestWelcomeContent(
    val id: String,
    val headingLines: List<String>,
    val panelTitle: String,
    val description: String,
    val imageUrl: String? = null,
    val imageAlignmentX: Float = .25f,
    val imageAlignmentY: Float = 0f,
) {
    companion object {
        val Local = GuestWelcomeContent(
            id = "local",
            headingLines = listOf("Ваш следующий", "старт — здесь"),
            panelTitle = "Ваш спортивный профиль",
            description = "Регистрации, участники и избранные старты — в одном месте.",
        )
    }
}
