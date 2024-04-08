package com.markettwits.registrations.registrations_list.data

import com.markettwits.registrations.registrations_list.domain.StartOrderInfo

interface StartOrderRegistrationRepository {
    suspend fun registrations(): Result<List<StartOrderInfo>>
    suspend fun pay(id: Int): Result<String>
}