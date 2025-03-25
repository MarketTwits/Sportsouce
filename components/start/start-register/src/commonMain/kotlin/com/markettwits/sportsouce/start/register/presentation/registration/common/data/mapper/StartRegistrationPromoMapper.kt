package com.markettwits.sportsouce.start.register.presentation.registration.common.data.mapper

import com.markettwits.sportsouce.start.register.domain.StartPromo
import com.markettwits.sportsouce.start.cloud.model.register.promocode.PromocodeResponse

class StartRegistrationPromoMapper  {
    fun map(promoCodeRemote: PromocodeResponse): StartPromo =
        StartPromo(
            success = promoCodeRemote.success,
            discountPercent = promoCodeRemote.promocode?.value ?: 0
        )
}