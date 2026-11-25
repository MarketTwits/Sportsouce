package com.markettwits.sportsouce.notification.impl.domain

internal data class PushMessage(
    val deliveryId: Int,
    val recipientUserId: Int,
    val title: String,
    val body: String,
) {
    companion object {
        fun parse(data: Map<String, String>): PushMessage? {
            if (data["kind"] != "sportsauce_notification") return null
            val deliveryId = data["deliveryId"].positiveInt() ?: return null
            val userId = data["recipientUserId"].positiveInt() ?: return null
            val title = data["title"]?.trim()?.takeIf(String::isNotBlank) ?: return null
            val body = data["body"]?.trim()?.takeIf(String::isNotBlank) ?: return null
            return PushMessage(deliveryId, userId, title, body)
        }

        private fun String?.positiveInt(): Int? = this?.toIntOrNull()?.takeIf { it > 0 }
    }
}
