package com.markettwits.start.register.presentation.order.domain.validation

import com.markettwits.core_ui.items.result.flatMapCallback
import com.markettwits.start.register.presentation.member.domain.RegistrationMemberValidator
import com.markettwits.start.register.presentation.order.domain.OrderStatement

class OrderValidationBase(private val registrationMemberValidator: RegistrationMemberValidator) :
    OrderValidationAbstract(registrationMemberValidator) {
    override fun validate(orderStatement: OrderStatement): Result<OrderStatement> =
        validateMembers(orderStatement).flatMapCallback {
            validateContacts(it)
        }
}