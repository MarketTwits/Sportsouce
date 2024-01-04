package com.markettwits.registrations.data

import com.markettwits.registrations.presentation.RegistrationsStore

interface RegistrationsDataSource {
    suspend fun registrations() : Result<RegistrationsStore.State>
}