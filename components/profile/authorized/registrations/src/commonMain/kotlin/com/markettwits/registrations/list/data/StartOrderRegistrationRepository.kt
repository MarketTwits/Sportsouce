package com.markettwits.registrations.list.data

import com.markettwits.registrations.list.domain.StartOrderInfo
import com.markettwits.registrations.list.domain.StartOrderPrice

interface StartOrderRegistrationRepository {

    suspend fun registrations(): Result<List<StartOrderInfo>>

    suspend fun pay(id: Int): Result<String>

    suspend fun getPrice(orderId : Int, orderDistancesId : List<String>, startId : Int) : Result<StartOrderPrice>
}