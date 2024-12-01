package com.markettwits.start_cloud.model.start

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
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