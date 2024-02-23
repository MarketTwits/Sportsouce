package com.markettwits.registrations.registrations.data.mapper

import com.markettwits.cloud.model.start_user.RemoteStartsUserItem
import com.markettwits.registrations.registrations.domain.StartOrderInfo

interface UserRegistrationsMapper {
    fun map(starts: List<RemoteStartsUserItem>): List<StartOrderInfo>
}