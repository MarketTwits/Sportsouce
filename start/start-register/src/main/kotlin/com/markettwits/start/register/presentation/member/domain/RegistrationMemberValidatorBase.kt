package com.markettwits.start.register.presentation.member.domain

import com.markettwits.start.register.domain.StartStatement

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