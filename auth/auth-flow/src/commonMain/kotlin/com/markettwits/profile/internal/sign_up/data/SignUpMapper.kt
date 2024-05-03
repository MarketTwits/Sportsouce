package com.markettwits.profile.internal.sign_up.data

import com.markettwits.cloud.model.sign_up.SignUpRequest
import com.markettwits.profile.internal.sign_up.domain.model.SignUpStatement

internal interface SignUpMapper {
    fun mapToRemote(statement: SignUpStatement): SignUpRequest

    /** convert phone in +7 (xxx) xxx-xx-xx to 7xxxxxxxxxx */
    fun mapPhoneToRemote(phone: String): String
}