package com.markettwits.sportsouce.profile.registrations.list.data.mapper

import com.markettwits.sportsouce.profile.cloud.model.registrations.UserRegistration
import com.markettwits.sportsouce.profile.cloud.model.start_price.StartPriceResponse
import com.markettwits.sportsouce.profile.registrations.list.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.list.domain.StartOrderPrice

interface UserRegistrationsMapper {

    fun map(start : UserRegistration) : StartOrderInfo

    fun map(registrations : List<UserRegistration>) : List<StartOrderInfo>

    fun mapPrice(priceResponse: StartPriceResponse) : StartOrderPrice

}