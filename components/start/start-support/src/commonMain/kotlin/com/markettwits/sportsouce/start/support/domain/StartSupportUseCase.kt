package com.markettwits.sportsouce.start.support.domain

interface StartSupportUseCase {
    suspend fun donation(startId: Int, price: String): Result<String>
    fun validate(price: String): Result<Int>
}