package com.markettwits.start_cloud.api.register

import com.markettwits.start_cloud.model.register.on_start.StartRegistrationResponse
import com.markettwits.start_cloud.model.register.price.StartRegisterPriceRequest
import com.markettwits.start_cloud.model.register.price.StartRegisterPriceResponse

interface SportSauceStartRegisterApi {

    suspend fun price(
        request: StartRegisterPriceRequest,
        token : String,
    ) : StartRegisterPriceResponse

    suspend fun register(
        request: StartRegisterPriceRequest,
        token : String
    ) : StartRegistrationResponse

}