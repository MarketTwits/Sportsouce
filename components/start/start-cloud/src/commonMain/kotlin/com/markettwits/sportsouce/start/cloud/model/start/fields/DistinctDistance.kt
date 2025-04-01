package com.markettwits.sportsouce.start.cloud.model.start.fields

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DistinctDistance(
    @SerialName("additional_fields")
    val additionalFields: List<AdditionalField>?,
    @SerialName("combo_discount_is_percent")
    val comboDiscountIsPercent: Boolean?,
    @SerialName("combo_discount_value")
    val comboDiscountValue: Int?,
    @SerialName("description")
    val description: String?,
    @SerialName("format")
    val format: String?,
    @SerialName("id")
    val id: Int,
    @SerialName("infinite_slots")
    val infiniteSlots: Boolean,
    @SerialName("name")
    val name: String,
    @SerialName("open_slots")
    val openSlots: Int?,
    @SerialName("order_number")
    val orderNumber: Int,
    @SerialName("slots")
    val slots: Int?,
    @SerialName("stages")
    val stages: List<Stage>,
    @SerialName("static_price")
    val staticPrice: Int?,
    @SerialName("taken_slots")
    val takenSlots: Int? = null,
)