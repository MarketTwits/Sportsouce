package com.markettwits.sportsouce.start.presentation.membres.list.component

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.markettwits.sportsouce.start.presentation.membres.list.models.StartMembersUi

class MockStartMembersScreen : StartMembersScreen {
    override val members: Value<List<StartMembersUi>> = MutableValue(emptyList())
    override val filterValue: Value<String> = MutableValue("")
    override fun handleTextFiled(value: String) = Unit
    override fun openFilter() = Unit
    override fun back() = Unit
}