package com.markettwits.inappnotification.api.model


import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonNull
import kotlinx.serialization.json.jsonObject


sealed interface InAppNotification {

    data object SelfUpdateStarted : InAppNotification

    data class SelfUpdateError(val exception: Exception) : InAppNotification

    data class HiddenApp(val action: () -> Unit) : InAppNotification

    data class SelfUpdateReady(
        val action: () -> Unit,
        val actualVersion: String,
        val description: String
    ) : InAppNotification

    data class Error(
        val titleId: Int,
        val descId: Int,
        val actionTextId: Int?,
        val action: (() -> Unit)?,
    ) : InAppNotification
}
