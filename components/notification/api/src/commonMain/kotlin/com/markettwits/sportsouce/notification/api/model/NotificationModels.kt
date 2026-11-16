package com.markettwits.sportsouce.notification.api.model

import kotlinx.serialization.Serializable

@Serializable
data class InboxNotification(
    val id: Int,
    val isRead: Boolean = false,
    val createdAt: String = "",
    val notification: NotificationContent,
)

@Serializable
data class NotificationContent(
    val title: String = "Уведомление",
    val text: String = "",
    val type: String = "",
    val templateKind: String? = null,
    val relatedStartId: Int? = null,
    val relatedStartName: String? = null,
    val recipientMeta: NotificationRecipient? = null,
) {
    val startId: Int? get() = (relatedStartId ?: recipientMeta?.relatedStartId ?: recipientMeta?.startId)?.takeIf { it > 0 }
    val startName: String? get() = relatedStartName?.takeIf(String::isNotBlank) ?: recipientMeta?.relatedStartName?.takeIf(String::isNotBlank)
    val isSystem: Boolean get() = type.startsWith("auto_") || type == "system"
    val kindLabel: String get() = when (templateKind) {
        "start_completed" -> "Старт завершён"
        "start_reminder" -> "Скоро старт"
        "registration_open" -> "Регистрация открыта"
        "registration_closed" -> "Ожидание старта"
        "start_started" -> "Старт проходит"
        "start_postponed" -> "Старт отложен"
        else -> if (isSystem) "Системное" else "От организатора"
    }
}

@Serializable
data class NotificationRecipient(
    val startId: Int? = null,
    val relatedStartId: Int? = null,
    val relatedStartName: String? = null,
)

enum class NotificationFilter(val label: String) {
    All("Все"), Unread("Непрочитанные"), System("Системные"), Organizer("От организатора");
    fun matches(item: InboxNotification): Boolean = when (this) {
        All -> true
        Unread -> !item.isRead
        System -> item.notification.isSystem
        Organizer -> !item.notification.isSystem
    }
}
