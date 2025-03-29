package com.markettwits.sportsouce.start.cloud.model.start

import com.markettwits.sportsouce.start.cloud.model.start.fields.ConditionFile
import com.markettwits.sportsouce.start.cloud.model.start.fields.Distance
import com.markettwits.sportsouce.start.cloud.model.start.fields.DistinctDistance
import com.markettwits.sportsouce.start.cloud.model.start.fields.Organizer
import com.markettwits.sportsouce.start.cloud.model.start.fields.PosterLinkFile
import com.markettwits.sportsouce.start.cloud.model.start.fields.Result
import com.markettwits.sportsouce.start.cloud.model.start.fields.StartStatus
import com.markettwits.sportsouce.start.cloud.model.start.fields.UsefulLinks
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
    @SerialName("conditionFile")
    val conditionFile: ConditionFile?,
    @SerialName("condition_short")
    val conditionShort: String?,
    @SerialName("coordinates")
    val coordinates: String?,
    @SerialName("description")
    val description: String?,
    @SerialName("distances")
    val distances: List<Distance>,
    @SerialName("distinctDistances")
    val distinctDistances: Map<Int, DistinctDistance>,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("organizers")
    val organizers: List<Organizer>,
    @SerialName("payment_disabled")
    val paymentDisabled: Boolean?,
    @SerialName("payment_type")
    val paymentType: String?,
    @SerialName("posterLinkFile")
    val posterLinkFile: PosterLinkFile?,
    @SerialName("reg_on_site")
    val regOnSite: Boolean? = null,
    @SerialName("short_name")
    val shortName: String?,
    @SerialName("slug")
    val slug: String?,
    @SerialName("start_date")
    val startDate: String,
    @SerialName("start_status")
    val startStatus: StartStatus,
    @SerialName("start_time")
    val startTime: String?,
    @SerialName("reg_link")
    val regLink : String?,
    @SerialName("useful_links")
    val usefulLinks: List<UsefulLinks>?,
    @SerialName("results")
    val results : List<Result>?
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