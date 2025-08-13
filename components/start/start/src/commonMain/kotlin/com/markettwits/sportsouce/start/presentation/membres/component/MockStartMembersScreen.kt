package com.markettwits.sportsouce.start.presentation.membres.component

import app.cash.paging.PagingData
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.markettwits.sportsouce.start.presentation.membres.store.StartMembersStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf

class MockStartMembersScreen : StartMembersScreen {
    override val filterValue: Value<String> = MutableValue("")
    override val state: StateFlow<StartMembersStore.State> =
        MutableStateFlow(
            StartMembersStore.State(
                membersItems = flowOf(PagingData.empty())
            )
        )

    override fun handleTextFiled(value: String) = Unit
    override fun removeSelectedFilter(title: String) = Unit
    override fun openFilter() = Unit
    override fun back() = Unit
}