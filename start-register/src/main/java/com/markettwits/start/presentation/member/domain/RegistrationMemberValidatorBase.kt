package com.markettwits.start.presentation.member.domain

import com.markettwits.start.domain.StartStatement

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