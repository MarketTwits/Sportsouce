package com.markettwits.start.presentation.membres

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class MockStartMembersScreen : StartMembersScreen {
    override val members: Value<List<StartMembersUi>> = MutableValue(emptyList())
    override fun filter(value: String) = Unit
    override fun back() = Unit
}