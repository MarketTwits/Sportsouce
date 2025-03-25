package com.markettwits.sportsouce.start.register.presentation.member.domain

import com.markettwits.sportsouce.start.register.domain.StartStatement

class RegistrationMemberValidatorBase : RegistrationMemberValidatorAbstract() {
    override fun validateFields(startStatement: StartStatement): Result<StartStatement> =
        runCatching {
            if (startStatement.contactPerson) {
                validateContactPerson(startStatement)
            } else {
                validateBasePerson(startStatement)
            }
            startStatement
        }
}