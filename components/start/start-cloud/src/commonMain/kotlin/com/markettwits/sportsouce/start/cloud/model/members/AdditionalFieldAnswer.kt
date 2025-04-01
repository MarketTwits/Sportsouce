package com.markettwits.sportsouce.start.cloud.model.members

import com.markettwits.sportsouce.start.cloud.model.start.fields.Option
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdditionalFieldAnswer(
    @SerialName("additionalFieldId")
    val additionalFieldId: Int,
    @SerialName("bool")
    val bool: Boolean?,
    @SerialName("date")
    val date: String?,
    @SerialName("id")
    val id: Int,
    @SerialName("memberStartId")
    val memberStartId: Int,
    @SerialName("multiSelect")
    val multiSelect: Boolean?,
    @SerialName("number")
    val number: Int?,
    @SerialName("options")
    val options: List<Option>?,
    @SerialName("price")
    val price: Int?,
    @SerialName("string")
    val string: String?,
    @SerialName("type")
    val type: String,
)