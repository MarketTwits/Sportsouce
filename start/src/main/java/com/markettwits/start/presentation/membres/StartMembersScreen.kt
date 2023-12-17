package com.markettwits.start.presentation.membres

import com.arkivanov.decompose.value.Value

interface StartMembersScreen {
    val members : Value<List<StartMembersUi>>
    fun back()
}