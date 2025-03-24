package com.markettwits.start_support.data

interface StartSupportRepository {
    suspend fun donation(startId: Int, price: Int): Result<String>
}