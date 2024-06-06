package com.markettwits.start.register.presentation.order.domain.validation

import com.markettwits.start.register.presentation.member.domain.RegistrationMemberValidator
import com.markettwits.start.register.presentation.order.domain.OrderStatement

abstract class OrderValidationAbstract(private val registrationMemberValidator: RegistrationMemberValidator) :
    OrderValidation {
    fun validateMembers(orderStatement: OrderStatement): Result<OrderStatement> {

        orderStatement.distanceInfo.map { it.members }.forEachIndexed { index, startStatement ->
            val validationResult = startStatement.map {
                registrationMemberValidator.validateFields(it)
            }
            if (validationResult.any { it.isFailure }) {
                val errorMessage =
                    "Участник ${index + 1} : ${
                        validationResult.first()
                            .exceptionOrNull()?.message ?: "Заполните информацию об участниках"
                    }"
                return Result.failure(IllegalArgumentException(errorMessage))
            }
        }
        return Result.success(orderStatement)
    }

    fun validateContacts(orderStatement: OrderStatement): Result<OrderStatement> =
        if (!orderStatement.distanceInfo.map { it.members }.any { it.any { it.contactPerson } }) {
            Result.failure(IllegalArgumentException("Хотя бы один участник должен быть контактным лицом"))
        } else {
            Result.success(orderStatement)
        }
}