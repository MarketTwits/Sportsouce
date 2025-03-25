package com.markettwits.sportsouce.starts.random.data

interface RandomRepository {
    suspend fun randomStart() : Result<Int>
}