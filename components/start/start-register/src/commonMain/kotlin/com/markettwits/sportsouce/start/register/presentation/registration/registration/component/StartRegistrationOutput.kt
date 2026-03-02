package com.markettwits.sportsouce.start.register.presentation.registration.registration.component

import com.markettwits.sportsouce.start.register.domain.StartStatement

interface StartRegistrationOutput{

    fun goBack()

    fun goSuccess()

    fun goAuth()

    fun openMember(
        stageId: Int,
        memberId: Int,
        startStatement: StartStatement,
    )

}
