package com.markettwits.registrations.list.data

import com.markettwits.registrations.list.domain.StartOrderInfo

interface StartOrderRegistrationRepository {
    suspend fun registrations(): Result<List<StartOrderInfo>>
    suspend fun pay(id: Int): Result<String>
}