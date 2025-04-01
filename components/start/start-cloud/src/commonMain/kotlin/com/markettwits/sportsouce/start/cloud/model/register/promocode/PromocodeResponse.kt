package com.markettwits.sportsouce.start.cloud.model.register.promocode

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PromocodeResponse(
    @SerialName("promocode")
    val promocode: Promocode?,
    @SerialName("success")
    val success: Boolean
){
    @Serializable
    data class Promocode(
        @SerialName("code")
        val code: String,
        @SerialName("id")
        val id: Int,
        @SerialName("isDisposable")
        val isDisposable: Boolean,
        @SerialName("start_id")
        val startId: Int,
        @SerialName("value")
        val value: Int
    )
}