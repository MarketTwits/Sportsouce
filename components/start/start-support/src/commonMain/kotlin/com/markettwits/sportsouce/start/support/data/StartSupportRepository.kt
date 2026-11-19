package com.markettwits.sportsouce.start.support.data

import com.markettwits.sportsouce.start.support.domain.StartDonationCheck
import com.markettwits.sportsouce.start.support.domain.CachedStartRegistration

interface StartSupportRepository {
    suspend fun cachedRegistration(startId: Int): Result<CachedStartRegistration>
    suspend fun donation(startId: Int, price: Int): Result<String>
    suspend fun checkDonation(startId: Int, refreshRegistrations: Boolean = false): Result<StartDonationCheck>
    suspend fun payRegistration(groupId: Int): Result<String>
}
