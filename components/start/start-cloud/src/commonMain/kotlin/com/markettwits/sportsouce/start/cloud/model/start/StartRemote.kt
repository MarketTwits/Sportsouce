package com.markettwits.sportsouce.start.cloud.model.start

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

@Serializable(with = StartRemote.StartItemRemoteItemSerializer::class)
sealed interface StartRemote {

    object StartItemRemoteItemSerializer :
        JsonContentPolymorphicSerializer<StartRemote>(StartRemote::class) {
        override fun selectDeserializer(element: JsonElement) = when {
            "start_data" in element.jsonObject -> StartRemoteOld.serializer()
            else -> StartRemoteNew.serializer()
        }
    }

}