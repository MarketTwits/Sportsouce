package com.markettwits.registrations.list.data.mapper

import com.markettwits.cloud.model.start_user.values.UserRegistrationNew
import com.markettwits.cloud.model.start_user.values.UserRegistrationOld
import com.markettwits.cloud.model.start_user.values.UserRegistrationShared
import com.markettwits.registrations.list.domain.StartOrderInfo

interface UserRegistrationsMapper {

    fun map(start : UserRegistrationNew) : StartOrderInfo

    fun map(start: UserRegistrationOld): StartOrderInfo

    fun map(registrations : List<UserRegistrationShared>) : List<StartOrderInfo>

}