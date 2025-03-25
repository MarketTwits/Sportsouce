package com.markettwits.sportsouce.start.support.data

interface StartSupportRepository {
    suspend fun donation(startId: Int, price: Int): Result<String>
}