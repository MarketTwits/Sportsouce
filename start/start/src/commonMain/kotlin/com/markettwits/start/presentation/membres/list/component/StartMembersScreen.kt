package com.markettwits.start.presentation.membres.list.component

import com.arkivanov.decompose.value.Value
import com.markettwits.start.presentation.membres.list.models.StartMembersUi

interface StartMembersScreen {
    val members: Value<List<StartMembersUi>>
    val filterValue: Value<String>
    fun handleTextFiled(value: String)
    fun openFilter()
    fun back()
}