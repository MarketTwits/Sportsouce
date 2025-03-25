package com.markettwits.sportsouce.start.cloud.model.start

import com.markettwits.sportsouce.start.cloud.model.start.fields.ConditionFile
import com.markettwits.sportsouce.start.cloud.model.start.fields.Distance
import com.markettwits.sportsouce.start.cloud.model.start.fields.DistinctDistance
import com.markettwits.sportsouce.start.cloud.model.start.fields.Organizer
import com.markettwits.sportsouce.start.cloud.model.start.fields.PosterLinkFile
import com.markettwits.sportsouce.start.cloud.model.start.fields.StartStatus
import com.markettwits.sportsouce.start.cloud.model.start.fields.UsefulLinks
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class StartRemoteNew(
    val conditionFile: ConditionFile?,
    val condition_short: String?,
    val coordinates: String?,
    val description: String?,
    val distances: List<Distance>,
    val distinctDistances: Map<Int, DistinctDistance>,
    val id: Int,
    val name: String,
    val organizers: List<Organizer>,
    val payment_disabled: Boolean?,
    val payment_type: String?,
    val posterLinkFile: PosterLinkFile?,
    val reg_on_site: Boolean? = null,
    val short_name: String?,
    val slug: String?,
    val start_date: String,
    val start_status: StartStatus,
    val start_time: String?,
    val reg_link : String?,
    val useful_links: List<UsefulLinks>?
) : StartRemote {

    @Deprecated("Use for isOpen field, don't use now")
    object BooleanAsStringOrBooleanSerializer : KSerializer<Boolean> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("BooleanAsStringOrBoolean", PrimitiveKind.STRING)
        override fun deserialize(decoder: Decoder): Boolean {
            return when (val value = decoder.decodeString()) {
                "true", "1" -> true
                "false", "0" -> false
                else -> throw IllegalArgumentException("Unexpected value: $value")
            }
        }
        override fun serialize(encoder: Encoder, value: Boolean) {
            encoder.encodeString(value.toString())
        }
    }
}