package com.markettwits.start.presentation.member.domain

import com.markettwits.start.domain.StartStatement

interface RegistrationMemberValidator {
    fun validateFields(startStatement: StartStatement): Result<StartStatement>
}