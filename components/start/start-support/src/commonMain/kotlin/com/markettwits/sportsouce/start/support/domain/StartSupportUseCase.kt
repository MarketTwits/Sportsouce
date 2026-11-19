package com.markettwits.sportsouce.start.support.domain

interface StartSupportUseCase {
    suspend fun donation(startId: Int, price: String): Result<String>
    suspend fun cachedRegistration(startId: Int): Result<CachedStartRegistration>
    suspend fun checkDonation(startId: Int, refreshRegistrations: Boolean = false): Result<StartDonationCheck>
    suspend fun payRegistration(groupId: Int): Result<String>
    fun validate(price: String): Result<Int>
}
