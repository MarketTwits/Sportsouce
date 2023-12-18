package com.markettwits.cloud.model.start_member

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class StartMemberItem(
    val city: String?,
    val contactPerson: Boolean?,
    val day: Int,
    val distance: String,
    val email: String? = "",
    val format: String,
    val gender: String,
    val group: String,
    val id: Int,
    val instagram: String,
    val name: String,
    val order_number: String?,
    val payment: Int?,
    val phone: String? = "",
    val price: Int,
    val reg_code: String,
    val sberbank_id: String?,
    val start_id: Int?,
    val surname: String,
    val team: String,
    val teamNumber: Int,
    val updatedAt: String,
    val user_id: Int?,
){
    @Serializable
    data class Group(
        @SerialName("name") val name: String,
        @SerialName("sex") @Serializable(with = UserListSerializer::class) val sex: List<String>,
        @SerialName("ageFrom") val ageFrom: String,
        @SerialName("ageTo") val ageTo: String
    )


    fun mapStartMember(text: String): Group {
        val json = Json {
            ignoreUnknownKeys = true
        }
        return json.decodeFromString<Group>(text)
    }
}
object UserListSerializer : JsonTransformingSerializer<List<String>>(ListSerializer(String.serializer())) {
    // If response is not an array, then it is a single object that should be wrapped into the array
    override fun transformDeserialize(element: JsonElement): JsonElement =
        if (element !is JsonArray) JsonArray(listOf(element)) else element
}