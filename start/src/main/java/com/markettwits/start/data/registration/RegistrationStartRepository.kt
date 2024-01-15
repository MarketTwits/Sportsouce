package com.markettwits.start.data.registration

import com.markettwits.start.domain.StartStatement

interface RegistrationStartRepository {
    suspend fun preload() : Result<StartStatement>
}