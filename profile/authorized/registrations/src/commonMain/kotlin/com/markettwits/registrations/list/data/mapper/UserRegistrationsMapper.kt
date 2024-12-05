package com.markettwits.registrations.list.data.mapper

import com.markettwits.cloud.model.start_user.values.UserRegistration
import com.markettwits.registrations.list.domain.StartOrderInfo

interface UserRegistrationsMapper {

    fun map(start : UserRegistration) : StartOrderInfo

    fun map(registrations : List<UserRegistration>) : List<StartOrderInfo>

}