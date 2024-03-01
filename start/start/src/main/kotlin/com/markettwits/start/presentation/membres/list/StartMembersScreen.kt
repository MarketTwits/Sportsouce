package com.markettwits.start.presentation.membres.list

import com.arkivanov.decompose.value.Value

interface StartMembersScreen {
    val members: Value<List<StartMembersUi>>
    val filterValue: Value<String>
    fun handleTextFiled(value: String)
    fun openFilter()
    fun back()
}