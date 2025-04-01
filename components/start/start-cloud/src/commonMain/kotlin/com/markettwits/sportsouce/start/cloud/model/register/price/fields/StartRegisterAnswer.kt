package com.markettwits.sportsouce.start.cloud.model.register.price.fields

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartRegisterAnswer(
    @SerialName("additionalFieldId")
    val additionalFieldId: Int,
    @SerialName("isOptional")
    val isOptional: Boolean,
    @SerialName("type")
    val type: String,
    @SerialName("string")
    val string : String? = null,
    @SerialName("number")
    val number : Int? = null,
    @SerialName("bool")
    val bool : Boolean? = null,
    @SerialName("date")
    val date : String? = null,
    @SerialName("multiSelect")
    val multiSelect : List<Int>? = null,
    @SerialName("singleSelect")
    val singleSelect: Int? = null,
)