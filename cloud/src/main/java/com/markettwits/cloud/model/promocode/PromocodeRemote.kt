package com.markettwits.cloud.model.promocode

import kotlinx.serialization.Serializable

@Serializable
data class PromocodeRemote(
    val promocode: Promocode?,
    val success: Boolean
){
    @Serializable
    data class Promocode(
        val code: String,
        val id: Int,
        val isDisposable: Boolean,
        val start_id: Int,
        val value: Int
    )
}