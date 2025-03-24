package com.markettwits.start.register.presentation.registration.common.data.mapper

import com.markettwits.start.register.domain.StartPromo
import com.markettwits.start_cloud.model.register.promocode.PromocodeResponse

class StartRegistrationPromoMapper  {
    fun map(promoCodeRemote: PromocodeResponse): StartPromo =
        StartPromo(
            success = promoCodeRemote.success,
            discountPercent = promoCodeRemote.promocode?.value ?: 0
        )
}