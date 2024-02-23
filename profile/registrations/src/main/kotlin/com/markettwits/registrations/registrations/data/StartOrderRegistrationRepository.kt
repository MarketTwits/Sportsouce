package com.markettwits.registrations.registrations.data

import com.markettwits.registrations.registrations.presentation.RegistrationsStore

interface StartOrderRegistrationRepository {
    suspend fun registrations() : Result<RegistrationsStore.State>
    suspend fun pay(id : Int) : Result<String>
}