package com.markettwits.sportsouce.profile.registrations.list.data

import com.markettwits.sportsouce.profile.registrations.list.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.list.domain.StartOrderPrice

interface StartOrderRegistrationRepository {

    suspend fun registrations(): Result<List<StartOrderInfo>>

    suspend fun pay(id: Int): Result<String>

    suspend fun getPrice(orderId : Int, orderDistancesId : List<String>, startId : Int) : Result<StartOrderPrice>
}