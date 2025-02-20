package com.markettwits.start_cloud.model.register.price.fields

import kotlinx.serialization.Serializable

@Serializable
data class StartRegisterAnswer(
    val additionalFieldId: Int,
    val isOptional: Boolean,
    val type: String,
    val string : String? = null,
    val number : Int? = null,
    val bool : Boolean? = null,
    val date : String? = null,
    val multiSelect : List<Int>? = null,
    val singleSelect: Int? = null,
)