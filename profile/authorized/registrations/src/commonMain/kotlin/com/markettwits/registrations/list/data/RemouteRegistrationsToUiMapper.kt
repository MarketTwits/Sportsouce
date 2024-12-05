package com.markettwits.registrations.list.data

import com.markettwits.cloud.model.start_user.values.UserRegistration
import com.markettwits.registrations.list.data.mapper.UserRegistrationsMapper
import com.markettwits.registrations.list.presentation.store.RegistrationsStore

interface RemoteRegistrationsToUiMapper {

    fun map(cloud: List<UserRegistration>): RegistrationsStore.State

    class Base(
        private val userRegistrationsMapper: UserRegistrationsMapper
    ) : RemoteRegistrationsToUiMapper {
        override fun map(cloud: List<UserRegistration>): RegistrationsStore.State {
            val base = userRegistrationsMapper.map(cloud)
            return RegistrationsStore.State(
                base = base,
            )
        }
    }
}