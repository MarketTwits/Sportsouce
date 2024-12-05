package com.markettwits.start_cloud.model.start

import com.markettwits.start_cloud.model.start.fields.ConditionFile
import com.markettwits.start_cloud.model.start.fields.Distance
import com.markettwits.start_cloud.model.start.fields.DistinctDistance
import com.markettwits.start_cloud.model.start.fields.Organizer
import com.markettwits.start_cloud.model.start.fields.PosterLinkFile
import com.markettwits.start_cloud.model.start.fields.StartStatus
import com.markettwits.start_cloud.model.start.fields.UsefulLinks
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class StartRemoteNew(
    //val afishaLinkFile: ConditionFile?,
    val conditionFile: ConditionFile?,
    val condition_short: String?,
    val coordinates: String?,
    val description: String?,
    val distances: List<Distance>,
    val distinctDistances: Map<Int, DistinctDistance>,
    val id: Int,
    @Serializable(with = BooleanAsStringOrBooleanSerializer::class)
    @SerialName("isOpen")
    val isOpen: Boolean,
    val name: String,
    val organizers: List<Organizer>,
    val payment_disabled: Boolean?,
    val payment_type: String?,
    val posterLinkFile: PosterLinkFile?,
    val reg_on_site: Boolean,
    val short_name: String?,
    val slug: String?,
    val start_date: String,
    val start_status: StartStatus,
    val start_time: String?,
    val reg_link : String?,
    val useful_links: List<UsefulLinks>?
) : StartRemote{

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