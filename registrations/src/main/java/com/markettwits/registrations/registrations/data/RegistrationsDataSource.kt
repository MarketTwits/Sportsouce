package com.markettwits.registrations.registrations.data

import com.markettwits.registrations.registrations.presentation.RegistrationsStore

interface RegistrationsDataSource {
    suspend fun registrations() : Result<RegistrationsStore.State>
}