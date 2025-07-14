package com.markettwits.sportsouce.profile.registrations.data

import com.markettwits.sportsouce.auth.service.api.SharedUser
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderPrice

interface StartOrderRegistrationRepository {

    suspend fun registrations(): Result<List<StartOrderInfo>>

    suspend fun currentUser() : Result<SharedUser>

    suspend fun pay(id: Int): Result<String>

    suspend fun getPrice(orderId : Int, orderDistancesId : List<String>, startId : Int) : Result<StartOrderPrice>
}