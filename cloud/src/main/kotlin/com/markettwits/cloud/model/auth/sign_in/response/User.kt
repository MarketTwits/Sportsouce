package com.markettwits.cloud.model.auth.sign_in.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

@Serializable
data class User(
    val address: String?,
    val age: String?,
    val birthday: String,
    val comment_for_address: String?,
    val createdAt: String,
    val email: String,
    val favor: String?,
    val id: Int,
    val instagram: String?,
    val name: String,
    val number: String,
    val patronymic: String?,
    val photo_id: Int?,
    val photo: Photo? = null,
    val role: String,
    val sex: String,
    val surname: String,
    val team: String?,
    val telegram: String?,
    val updatedAt: String,
    val verification_code: String,
    val verified: Boolean,
    val vk: String?,
    val whatsapp: String?
) {
    @Serializable(with = PhotoSerializer::class)
    sealed class Photo {
        @Serializable
        @SerialName("EmptyPhoto")
        object EmptyPhoto : Photo()

        @Serializable
        data class WithPhoto(val id: Int, val name: String, val path: String) : Photo() {
            fun imageUrl() = "$path/$name"
        }

    }
    object PhotoSerializer : JsonContentPolymorphicSerializer<Photo>(Photo::class) {
        override fun selectDeserializer(element: JsonElement) = when {
            element is JsonObject && element.isEmpty() -> Photo.EmptyPhoto.serializer()
            else -> Photo.WithPhoto.serializer()
        }
    }
}