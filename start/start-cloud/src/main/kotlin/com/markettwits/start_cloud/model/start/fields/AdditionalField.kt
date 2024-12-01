package com.markettwits.start_cloud.model.start.fields

import kotlinx.serialization.Serializable

@Serializable
data class AdditionalField(
    val id: Int,
    val stage_id: Int?,
    val parent_id : Int?,
    val parent_uuid : String?,
    val dependent_fields: List<AdditionalField>? = null,
    val distance_id: Int?,
    val expected_answer_id: Int?,
    val expected_answer_uuid: String?,
    val is_optional: Boolean,
    val is_public: Boolean,
    val name: String,
    val options: List<Option>,
    val price: Int?,
    val type: String,
    val uuid: String
)