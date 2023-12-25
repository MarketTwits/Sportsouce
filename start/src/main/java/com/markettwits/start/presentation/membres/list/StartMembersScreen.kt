package com.markettwits.start.presentation.membres.list

import com.arkivanov.decompose.value.Value

interface StartMembersScreen {
    val members : Value<List<StartMembersUi>>
    fun filter(value : String)
    fun openFilter()
    fun back()
}