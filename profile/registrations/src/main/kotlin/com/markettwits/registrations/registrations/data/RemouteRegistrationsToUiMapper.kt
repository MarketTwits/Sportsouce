package com.markettwits.registrations.registrations.data

import com.markettwits.cloud.model.start_user.RemoteStartsUserItem
import com.markettwits.registrations.registrations.data.mapper.UserRegistrationsMapper
import com.markettwits.registrations.registrations.presentation.store.RegistrationsStore

interface RemoteRegistrationsToUiMapper {
    fun map(cloud: List<RemoteStartsUserItem>): RegistrationsStore.State

    class Base(
        private val userRegistrationsMapper: UserRegistrationsMapper
    ) : RemoteRegistrationsToUiMapper {
        override fun map(cloud: List<RemoteStartsUserItem>): RegistrationsStore.State {
            val base = userRegistrationsMapper.map(cloud)
            return RegistrationsStore.State(
                base = base,
            )
        }
    }
}