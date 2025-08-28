package com.markettwits.sportsouce.profile.registrations.presentation.list.component

import com.arkivanov.decompose.value.Value
import com.markettwits.sportsouce.profile.registrations.presentation.list.components.filter.FilterItem
import com.markettwits.sportsouce.profile.registrations.presentation.list.store.RegistrationsStore
import kotlinx.coroutines.flow.StateFlow

interface RegistrationsComponent {
    val value: StateFlow<RegistrationsStore.State>
    fun obtainEvent(event: RegistrationsStore.Intent)
    val showFilterDialog: Value<Boolean>
    fun openFilter()
    fun closeFilter()
    fun toggleFilter(item: FilterItem)
}