package com.markettwits.start.register.presentation.member.domain

import com.markettwits.start.register.domain.StartStatement

interface RegistrationMemberValidator {

    fun validateFields(startStatement: StartStatement): Result<StartStatement>

}