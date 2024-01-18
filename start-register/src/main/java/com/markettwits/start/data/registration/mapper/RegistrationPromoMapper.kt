package com.markettwits.start.data.registration.mapper

import com.markettwits.cloud.model.promocode.PromocodeRemote
import com.markettwits.start.domain.StartPromo

interface RegistrationPromoMapper {
    fun map(promocodeRemote: PromocodeRemote): StartPromo
}

class RegistrationPromoMapperBase : RegistrationPromoMapper {
    override fun map(promocodeRemote: PromocodeRemote): StartPromo =
        StartPromo(
            success = promocodeRemote.success,
            discountPercent = promocodeRemote.promocode?.value ?: 0
        )
}