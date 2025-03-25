package com.markettwits.sportsouce.start.cloud.model.result


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.encodeToJsonElement

@Serializable
data class StartMemberResult(
    @SerialName("birthday")
    val birthday: String = "",
    @SerialName("body_number")
    val bodyNumber: String = "",
    @SerialName("circles")
    @Serializable(with = CirclesSerializer::class)
    val circles: Map<Int, String>,
    @SerialName("createdAt")
    val createdAt: String = "",
    @SerialName("distance")
    val distance: String = "",
    @SerialName("group")
    val group: String = "",
    @SerialName("id")
    val id: Int = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("place")
    val place: Int? = null,
    @SerialName("registration_id")
    val registrationId: Int? = null,
    @SerialName("result")
    val result: String = "",
    @SerialName("sex")
    val sex: String = "",
    @SerialName("shift")
    val shift: String = "",
    @SerialName("start_id")
    val startId: Int = 0,
    @SerialName("team")
    val team: String = "",
    @SerialName("updatedAt")
    val updatedAt: String = "",
    @SerialName("user_id")
    val userId: Int? = null
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