package com.markettwits.sportsouce.profile.registrations.presentation.list.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.presentation.list.components.filter.FilterItem
import com.markettwits.sportsouce.profile.registrations.presentation.list.store.RegistrationsDataStoreFactory
import com.markettwits.sportsouce.profile.registrations.presentation.list.store.RegistrationsStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistrationsComponentBase(
    component: ComponentContext,
    private val storeFactory: RegistrationsDataStoreFactory,
    private val pop: () -> Unit,
    private val onItemClick: (itemId: StartOrderInfo) -> Unit
) : RegistrationsComponent, ComponentContext by component {

    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }
    private val scope = CoroutineScope(Dispatchers.Main)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val value: StateFlow<RegistrationsStore.State> = store.stateFlow

    override fun obtainEvent(event: RegistrationsStore.Intent) {
        store.accept(event)
    }

    // Filter dialog state management
    private val _showFilterDialog = MutableValue(false)

    override val showFilterDialog: Value<Boolean> = _showFilterDialog

    override fun openFilter() {
        _showFilterDialog.value = true
    }

    override fun closeFilter() {
        _showFilterDialog.value = false
    }

    override fun toggleFilter(item: FilterItem) {
        obtainEvent(RegistrationsStore.Intent.OnClickFilter(item))
    }

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is RegistrationsStore.Label.GoBack -> pop()
                    is RegistrationsStore.Label.OnItemClick -> onItemClick(it.itemId)
                }
            }
        }
    }
}