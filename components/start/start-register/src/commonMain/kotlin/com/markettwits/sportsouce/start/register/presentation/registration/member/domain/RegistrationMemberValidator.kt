package com.markettwits.sportsouce.start.register.presentation.registration.member.domain

import com.markettwits.sportsouce.start.register.domain.StartStatement

interface RegistrationMemberValidator {

    fun validateFields(startStatement: StartStatement): Result<StartStatement>

}