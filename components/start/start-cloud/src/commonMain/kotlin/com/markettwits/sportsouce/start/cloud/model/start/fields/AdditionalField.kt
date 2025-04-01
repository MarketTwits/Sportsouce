package com.markettwits.sportsouce.start.cloud.model.start.fields

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdditionalField(
    @SerialName("id")
    val id: Int,
    @SerialName("stage_id")
    val stage_id: Int?,
    @SerialName("parent_id")
    val parent_id : Int?,
    @SerialName("parent_uuid")
    val parent_uuid : String?,
    @SerialName("dependent_fields")
    val dependent_fields: List<AdditionalField>? = null,
    @SerialName("distance_id")
    val distance_id: Int?,
    @SerialName("expected_answer_id")
    val expected_answer_id: Int?,
    @SerialName("expected_answer_uuid")
    val expected_answer_uuid: String?,
    @SerialName("is_optional")
    val is_optional: Boolean,
    @SerialName("is_public")
    val is_public: Boolean,
    @SerialName("name")
    val name: String,
    @SerialName("options")
    val options: List<Option>,
    @SerialName("price")
    val price: Int?,
    @SerialName("type")
    val type: String,
    @SerialName("uuid")
    val uuid: String
)