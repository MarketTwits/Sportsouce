package com.markettwits.sportsouce.profile.registrations.data

import com.markettwits.sportsouce.auth.service.api.SharedUser
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderPrice
import kotlinx.coroutines.flow.Flow

interface StartOrderRegistrationRepository {

    fun registrations(forced: Boolean): Flow<List<StartOrderInfo>>

    suspend fun currentUser() : Result<SharedUser>

    suspend fun pay(id: Int): Result<String>

    suspend fun getPrice(orderId : Int, orderDistancesId : List<String>, startId : Int) : Result<StartOrderPrice>
}