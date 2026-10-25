package com.markettwits.sportsouce.start.support.domain

interface StartSupportUseCase {
    suspend fun donation(startId: Int, price: String): Result<String>
    suspend fun checkDonation(startId: Int): Result<StartDonationCheck>
    suspend fun payRegistration(groupId: Int): Result<String>
    fun validate(price: String): Result<Int>
}
