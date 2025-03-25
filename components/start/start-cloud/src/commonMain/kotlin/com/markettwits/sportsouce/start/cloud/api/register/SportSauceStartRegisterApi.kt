package com.markettwits.sportsouce.start.cloud.api.register

import com.markettwits.sportsouce.start.cloud.model.register.on_start.StartRegistrationResponse
import com.markettwits.sportsouce.start.cloud.model.register.price.StartRegisterPriceRequest
import com.markettwits.sportsouce.start.cloud.model.register.price.StartRegisterPriceResponse
import com.markettwits.sportsouce.start.cloud.model.register.promocode.PromocodeResponse

interface SportSauceStartRegisterApi {

    suspend fun price(
        request: StartRegisterPriceRequest,
        token : String,
    ) : StartRegisterPriceResponse

    suspend fun register(
        request: StartRegisterPriceRequest,
        token : String
    ) : StartRegistrationResponse

    suspend fun promo(value : String,startId: Int,distancesId : List<Int>) : PromocodeResponse

}