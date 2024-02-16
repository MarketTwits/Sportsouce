package com.markettwits.start.presentation.order.domain.validation

import com.markettwits.core_ui.result.flatMapCallback
import com.markettwits.start.presentation.member.domain.RegistrationMemberValidator
import com.markettwits.start.presentation.order.domain.OrderStatement

class OrderValidationBase(private val registrationMemberValidator: RegistrationMemberValidator) :
    OrderValidationAbstract(registrationMemberValidator) {
    override fun validate(orderStatement: OrderStatement): Result<OrderStatement> =
        validateMembers(orderStatement).flatMapCallback {
            validateContacts(it)
        }
}