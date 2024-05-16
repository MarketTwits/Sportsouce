package com.markettwits.start_support.domain

import com.markettwits.core_ui.items.result.flatMapCallback
import com.markettwits.start_support.data.StartSupportRepository

internal class StartSupportUseCaseBase(private val repository: StartSupportRepository) :
    StartSupportUseCase {
    override suspend fun donation(startId: Int, price: String): Result<String> =
        validate(price).flatMapCallback { currentPrice ->
            repository.donation(startId, currentPrice)
        }

    override fun validate(price: String): Result<Int> {
        if (!(price.all { it.isDigit() }))
            Result.failure<Int>(IllegalArgumentException("Сумма может быть только числом"))
        if (price.toInt() >= 1_000_000)
            Result.failure<Int>(IllegalArgumentException("Сумма не может быть больше 1 000 000 рублей"))
        return Result.success(price.toInt())
    }
}