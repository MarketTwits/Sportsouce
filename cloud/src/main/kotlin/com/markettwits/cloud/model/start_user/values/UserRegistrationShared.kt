package com.markettwits.cloud.model.start_user.values

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

@Serializable(with = UserRegistrationShared.UserRegistrationSerializer::class)
sealed interface UserRegistrationShared {

    object UserRegistrationSerializer :
        JsonContentPolymorphicSerializer<UserRegistrationShared>(UserRegistrationShared::class) {
        override fun selectDeserializer(element: JsonElement) = when {
            "members" in element.jsonObject -> UserRegistrationNew.serializer()
            else -> UserRegistrationOld.serializer()
        }
    }
}