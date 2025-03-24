package com.markettwits.registrations.list.data.mapper

import com.markettwits.cloud.model.start_price.StartPriceResponse
import com.markettwits.profile.cloud.model.registrations.UserRegistration
import com.markettwits.registrations.list.domain.StartOrderInfo
import com.markettwits.registrations.list.domain.StartOrderPrice

interface UserRegistrationsMapper {

    fun map(start : UserRegistration) : StartOrderInfo

    fun map(registrations : List<UserRegistration>) : List<StartOrderInfo>

    fun mapPrice(priceResponse: StartPriceResponse) : StartOrderPrice

}