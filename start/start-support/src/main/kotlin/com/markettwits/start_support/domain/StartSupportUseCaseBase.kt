package com.markettwits.start_support.domain

import com.markettwits.core_ui.result.flatMapCallback
import com.markettwits.start_support.data.StartSupportRepository

internal class StartSupportUseCaseBase(private val repository: StartSupportRepository) :
    StartSupportUseCase {
    override suspend fun donation(startId: Int, price: String): Result<String> =
        validate(price).flatMapCallback { currentPrice ->
            repository.donation(startId, currentPrice)
        }

    override fun validate(price: String): Result<Int> =
        if (price.all { it.isDigit() })
            Result.success(price.toInt())
        else Result.failure(IllegalArgumentException("Сумма может быть только числом"))
}