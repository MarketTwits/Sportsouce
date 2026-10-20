package com.markettwits.sportsouce.profile.cloud.model.identity

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

/** Older profiles can expose the birth year as a numeric string. */
internal object NullableIntOrStringSerializer : KSerializer<Int?> {
    override val descriptor = PrimitiveSerialDescriptor("BirthYear", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): Int? =
        ((decoder as JsonDecoder).decodeJsonElement() as? JsonPrimitive)?.intOrNull
    override fun serialize(encoder: Encoder, value: Int?) {
        (encoder as JsonEncoder).encodeJsonElement(value?.let(::JsonPrimitive) ?: JsonNull)
    }
}
