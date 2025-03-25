package com.markettwits.sportsouce.auth.cloud.model.sign_in.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

@Serializable
data class User(
    @SerialName("address")
    val address: String?,
    @SerialName("age")
    val age: String?,
    @SerialName("birthday")
    val birthday: String,
    @SerialName("comment_for_address")
    val commentForAddress: String?,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("email")
    val email: String? = null,
    @SerialName("id")
    val id: Int,
    @SerialName("instagram")
    val instagram: String?,
    @SerialName("name")
    val name: String,
    @SerialName("number")
    val number: String,
    @SerialName("patronymic")
    val patronymic: String?,
    @SerialName("photo_id")
    val photoId: Int?,
    @SerialName("photo")
    val photo: Photo? = null,
    @SerialName("sex")
    val sex: String,
    @SerialName("surname")
    val surname: String,
    @SerialName("team")
    val team: String?,
    @SerialName("telegram")
    val telegram: String?,
    @SerialName("vk")
    val vk: String?,
    @SerialName("whatsapp")
    val whatsapp: String?
) {
    @Serializable(with = PhotoSerializer::class)
    sealed class Photo {
        @Serializable
        @SerialName("EmptyPhoto")
        data object EmptyPhoto : Photo()

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