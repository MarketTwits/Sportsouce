package com.markettwits.registrations.registrations.data

import com.markettwits.registrations.registrations.domain.StartOrderInfo

interface StartOrderRegistrationRepository {
    suspend fun registrations(): Result<List<StartOrderInfo>>
    suspend fun pay(id: Int): Result<String>
}