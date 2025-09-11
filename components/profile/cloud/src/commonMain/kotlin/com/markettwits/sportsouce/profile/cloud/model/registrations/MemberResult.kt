package com.markettwits.sportsouce.profile.cloud.model.registrations


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.*

@Serializable
data class MemberResult(
    @SerialName("birthday")
    val birthday: String? = null,
    @SerialName("body_number")
    val bodyNumber: String? = null,
    @SerialName("circles")
    @Serializable(with = CirclesSerializer::class)
    val circles: Map<Int, String>? = null,
    @SerialName("createdAt")
    val createdAt: String? = null,
    @SerialName("distance")
    val distance: String? = null,
    @SerialName("group")
    val group: String? = null,
    @SerialName("id")
    val id: Int,
    @SerialName("member_start_id")
    val memberStartId: Int,
    @SerialName("name")
    val name: String? = null,
    @SerialName("place")
    val place: Int? = null,
    @SerialName("registration_id")
    val registrationId: Int? = null,
    @SerialName("result")
    val result: String? = null,
    @SerialName("sex")
    val sex: String? = null,
    @SerialName("shift")
    val shift: String? = null,
    @SerialName("start_id")
    val startId: Int,
    @SerialName("team")
    val team: String? = null,
    @SerialName("updatedAt")
    val updatedAt: String? = null,
    @SerialName("user_id")
    val userId: Int? = null,
)

object CirclesSerializer : JsonTransformingSerializer<Map<Int, String>>(
    MapSerializer(Int.serializer(), String.serializer())
) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        return if (element is JsonPrimitive && element.isString) {
            val list = Json.decodeFromString<List<String>>(element.content)
            val map = list.mapIndexed { index, value -> index + 1 to value }.toMap()
            Json.encodeToJsonElement(map)
        } else {
            element
        }
    }
}