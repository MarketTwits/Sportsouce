package com.markettwits.sportsouce.start.presentation.membres.component

import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.StateFlow
import com.markettwits.sportsouce.start.presentation.membres.store.StartMembersStore

interface StartMembersScreen {
    // Search query for StartSearchMember
    val filterValue: Value<String>

    // MVI state with Flow<PagingData<StartMembersUi>>
    val state: StateFlow<StartMembersStore.State>

    fun handleTextFiled(value: String)
    fun removeSelectedFilter(title: String)
    fun openFilter()
    fun back()
}