package com.markettwits.start_support.domain

import com.markettwits.core_ui.items.extensions.flatMapCallback
import com.markettwits.start_support.data.StartSupportRepository

internal class StartSupportUseCaseBase(private val repository: StartSupportRepository) :
    StartSupportUseCase {

    override suspend fun donation(startId: Int, price: String): Result<String> =
        validate(price).flatMapCallback { currentPrice ->
            repository.donation(startId, currentPrice)
        }

    override fun validate(price: String): Result<Int> {
        val currentPrice = price.trim()
        if (!(currentPrice.all { it.isDigit() }))
            return Result.failure<Int>(IllegalArgumentException("Сумма может быть только числом"))
        if (currentPrice.toInt() >= 100000)
            return Result.failure<Int>(IllegalArgumentException("Сумма не может быть больше 100000 рублей"))
        return Result.success(currentPrice.toInt())
    }
}