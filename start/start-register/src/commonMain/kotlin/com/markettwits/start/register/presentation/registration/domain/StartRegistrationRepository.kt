package com.markettwits.start.register.presentation.registration.domain

import com.markettwits.members.member_common.domain.ProfileMember
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationDistance
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationResult
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationStageWithStatement
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationStatementAnswer
import com.markettwits.start_cloud.model.register.price.StartRegisterPriceResponse
import com.markettwits.start_cloud.model.start.fields.DistinctDistance

interface StartRegistrationRepository {

    suspend fun registerOnStart(
        comboId : Int?,
        startId : Int,
        promo: String,
        registrationWithoutPayment : Boolean,
        distances: List<StartRegistrationDistance>,
    ) : Result<StartRegistrationResult>

    suspend fun getStartPrice(
        comboId : Int?,
        startId : Int,
        promo: String,
        distances: List<StartRegistrationDistance>,
    ) : Result<StartRegisterPriceResponse>

    suspend fun getStartDistances(distances : List<DistinctDistance>) : Result<List<StartRegistrationDistance>>

}