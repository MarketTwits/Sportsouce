package com.markettwits.sportsouce.start.register.presentation.registration.common.domain

import com.markettwits.sportsouce.start.cloud.model.start.fields.DistinctDistance
import com.markettwits.sportsouce.start.register.domain.StartPromo
import com.markettwits.sportsouce.start.register.domain.StartRegType
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationDistance
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationPriceResult
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationResult

interface StartRegistrationRepository {

    suspend fun isRegisterAvailable() : Result<Unit>

    suspend fun registerOnStart(
        comboId : Int?,
        startId : Int,
        promo: String,
        registrationWithoutPayment : Boolean,
        distances: List<StartRegistrationDistance>,
        regType: StartRegType,
    ) : Result<StartRegistrationResult>

    suspend fun getStartPrice(
        comboId : Int?,
        startId : Int,
        promo: String,
        distances: List<StartRegistrationDistance>,
        regType: StartRegType,
    ) : Result<StartRegistrationPriceResult>

    suspend fun getStartPromo(value: String, startId: Int,distancesId : List<Int>): Result<StartPromo>

    suspend fun getStartDistances(distances : List<DistinctDistance>) :
            Result<List<StartRegistrationDistance>>

}