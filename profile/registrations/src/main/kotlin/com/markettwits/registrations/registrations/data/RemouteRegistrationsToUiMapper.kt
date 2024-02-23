package com.markettwits.registrations.registrations.data

import com.markettwits.cloud.model.start_user.RemoteStartsUserItem
import com.markettwits.registrations.registrations.data.mapper.UserRegistrationsMapper
import com.markettwits.registrations.registrations.domain.StartOrderInfo
import com.markettwits.registrations.registrations.domain.StartPaymentState
import com.markettwits.registrations.registrations.presentation.RegistrationsStore

interface RemoteRegistrationsToUiMapper {
    fun map(cloud: List<RemoteStartsUserItem>): RegistrationsStore.State

    class Base(
        private val userRegistrationsMapper: UserRegistrationsMapper
    ) : RemoteRegistrationsToUiMapper {
        override fun map(cloud: List<RemoteStartsUserItem>): RegistrationsStore.State {
            val base = userRegistrationsMapper.map(cloud)
            return RegistrationsStore.State(
                info = base,
                paymentState = mapPaymentList(base),
            )
        }

        private fun mapPaymentList(base: List<StartOrderInfo>): StartPaymentState {
            val list = base
                .filter { !it.payment }
            val count = list.size
            val cost = list.sumOf { it.cost.toDoubleOrNull() ?: 0.0 }
            return StartPaymentState(
                paymentList = list,
                count = count,
                totalCost = cost.toInt()
            )
        }
    }
}