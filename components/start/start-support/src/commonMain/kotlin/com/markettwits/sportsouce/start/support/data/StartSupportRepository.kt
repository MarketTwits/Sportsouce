package com.markettwits.sportsouce.start.support.data

import com.markettwits.sportsouce.start.support.domain.StartDonationCheck

interface StartSupportRepository {
    suspend fun donation(startId: Int, price: Int): Result<String>
    suspend fun checkDonation(startId: Int): Result<StartDonationCheck>
    suspend fun payRegistration(groupId: Int): Result<String>
}
