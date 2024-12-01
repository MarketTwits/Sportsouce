package com.markettwits.start_cloud.model.start.fields

import kotlinx.serialization.Serializable

@Serializable
data class Option(
    val fieldId: Int?,
    val id: Int,
    val price: Int?,
    val title: String,
    val uuid: String? = null
)