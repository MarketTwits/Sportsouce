package com.markettwits.registrations.list.data.mapper

import com.markettwits.cloud.model.start_user.RemoteStartsUserItem
import com.markettwits.registrations.list.domain.StartOrderInfo

interface UserRegistrationsMapper {
    fun map(starts: List<RemoteStartsUserItem>): List<StartOrderInfo>
}