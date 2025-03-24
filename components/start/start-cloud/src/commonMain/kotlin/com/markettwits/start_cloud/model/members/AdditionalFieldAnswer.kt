package com.markettwits.start_cloud.model.members

import com.markettwits.start_cloud.model.start.fields.Option
import kotlinx.serialization.Serializable

@Serializable
data class AdditionalFieldAnswer(
    val additionalFieldId: Int,
    val bool: Boolean?,
    val date: String?,
    val id: Int,
    val memberStartId: Int,
    val multiSelect: Boolean?,
    val number: Int?,
    val options: List<Option>?,
    val price: Int?,
    val string: String?,
    val type: String,
)