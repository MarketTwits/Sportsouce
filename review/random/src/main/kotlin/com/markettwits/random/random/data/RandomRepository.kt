package com.markettwits.random.random.data

interface RandomRepository {
    suspend fun randomStart() : Result<Int>
}