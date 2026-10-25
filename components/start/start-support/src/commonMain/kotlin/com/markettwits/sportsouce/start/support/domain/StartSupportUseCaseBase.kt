package com.markettwits.sportsouce.start.support.domain

import com.markettwits.core_ui.items.extensions.flatMapCallback
import com.markettwits.sportsouce.start.support.data.StartSupportRepository

internal class StartSupportUseCaseBase(private val repository: StartSupportRepository) :
    StartSupportUseCase {

    override suspend fun checkDonation(startId: Int) = repository.checkDonation(startId)
    override suspend fun payRegistration(groupId: Int) = repository.payRegistration(groupId)

    override suspend fun donation(startId: Int, price: String): Result<String> =
        validate(price).flatMapCallback { currentPrice ->
            repository.donation(startId, currentPrice)
        }

    override fun validate(price: String): Result<Int> {
        val currentPrice = price.trim()
        if (currentPrice.isEmpty() || !currentPrice.all { it.isDigit() })
            return Result.failure<Int>(IllegalArgumentException("Сумма может быть только числом"))
        val amount = currentPrice.toIntOrNull()
            ?: return Result.failure(IllegalArgumentException("Проверьте сумму пожертвования"))
        if (amount <= 0)
            return Result.failure(IllegalArgumentException("Сумма должна быть больше нуля"))
        if (amount >= 100000)
            return Result.failure<Int>(IllegalArgumentException("Сумма не может быть больше 100000 рублей"))
        return Result.success(amount)
    }
}
