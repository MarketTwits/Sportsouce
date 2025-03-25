package com.markettwits.sportsouce.start.cloud.model.register.on_start

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@Serializable
data class StartRegistrationResponse(
    @Serializable(with = PaymentSerializer::class)
    val payment: Payment? = null,
    val success: Boolean
) {
    @Serializable
    @Polymorphic
    sealed class Payment {
        @Serializable
        data class PaymentString(
            val payment: String
        ) : Payment()

        @Serializable
        data class PaymentBase(
            val formUrl: String,
            val orderId: String
        ) : Payment()
    }

    object PaymentSerializer : KSerializer<Payment> {
        override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Payment") {
            element<String>("payment")
        }

        override fun serialize(encoder: Encoder, value: Payment) {
            when (value) {
                is Payment.PaymentString ->
                    encoder.encodeStructure(descriptor) {
                        encodeStringElement(descriptor, 0, value.payment)
                    }

                is Payment.PaymentBase ->
                    encoder.encodeStructure(descriptor) {
                        encodeSerializableElement(
                            descriptor,
                            0,
                            Payment.PaymentBase.serializer(),
                            value
                        )
                    }
            }
        }

        override fun deserialize(decoder: Decoder): Payment {
            val jsonDecoder = decoder as JsonDecoder
            val jsonElement = jsonDecoder.decodeJsonElement()

            return when {
                jsonElement is JsonPrimitive && jsonElement.isString ->
                    Payment.PaymentString(jsonElement.content)

                jsonElement is JsonObject ->
                    jsonDecoder.json.decodeFromJsonElement(
                        Payment.PaymentBase.serializer(),
                        jsonElement
                    )

                else -> throw SerializationException("Invalid JSON element for Payment")
            }
        }
    }
}