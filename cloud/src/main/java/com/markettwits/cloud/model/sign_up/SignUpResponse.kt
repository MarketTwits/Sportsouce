package com.markettwits.cloud.model.sign_up

import kotlinx.serialization.Serializable
import javax.xml.crypto.Data
@Serializable
data class SignUpResponse(
    val data: Data,
    val message: String,
    val success: Boolean
){
    @Serializable
    data class Data(
        val id: Int
    )
}