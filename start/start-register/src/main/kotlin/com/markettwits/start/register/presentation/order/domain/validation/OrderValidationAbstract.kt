package com.markettwits.start.register.presentation.order.domain.validation

import com.markettwits.start.register.presentation.member.domain.RegistrationMemberValidator
import com.markettwits.start.register.presentation.order.domain.OrderStatement

abstract class OrderValidationAbstract(private val registrationMemberValidator: RegistrationMemberValidator) :
    OrderValidation {
    fun validateMembers(orderStatement: OrderStatement): Result<OrderStatement> {
        orderStatement.members.forEachIndexed { index, startStatement ->
            val validationResult = registrationMemberValidator.validateFields(startStatement)
            if (validationResult.isFailure) {
                val errorMessage =
                    "Участник ${index + 1} : ${validationResult.exceptionOrNull()?.message ?: "Unknown error"}"
                return Result.failure(IllegalArgumentException(errorMessage))
            }
        }
        return Result.success(orderStatement)
    }

    fun validateContacts(orderStatement: OrderStatement): Result<OrderStatement> =
        if (!orderStatement.members.any { it.contactPerson }) {
            Result.failure(IllegalArgumentException("Хотя бы один участник должен быть контактным лицом"))
        } else {
            Result.success(orderStatement)
        }
}