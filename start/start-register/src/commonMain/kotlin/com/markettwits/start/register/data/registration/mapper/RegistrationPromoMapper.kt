package com.markettwits.start.register.data.registration.mapper

import com.markettwits.cloud.model.promocode.PromocodeRemote
import com.markettwits.start.register.domain.StartPromo

interface RegistrationPromoMapper {
    fun map(promoCodeRemote: PromocodeRemote): StartPromo
}

class RegistrationPromoMapperBase : RegistrationPromoMapper {
    override fun map(promoCodeRemote: PromocodeRemote): StartPromo =
        StartPromo(
            success = promoCodeRemote.success,
            discountPercent = promoCodeRemote.promocode?.value ?: 0
        )
}