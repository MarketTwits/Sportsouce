package com.markettwits.sportsouce.start.cloud.model.start.fields

import kotlinx.serialization.Serializable

@Serializable
data class DistinctDistance(
    val additional_fields: List<AdditionalField>?,
    val combo_discount_is_percent: Boolean?,
    val combo_discount_value: Int?,
    val description: String?,
    val format: String?,
    val id: Int,
    val infinite_slots: Boolean,
    val name: String,
    val open_slots: Int?,
    val order_number: Int,
    val slots: Int?,
    val stages: List<Stage>,
    val static_price: Int?,
    val taken_slots: Int? = null,
)