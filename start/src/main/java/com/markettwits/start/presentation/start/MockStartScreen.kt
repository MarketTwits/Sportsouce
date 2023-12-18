package com.markettwits.start.presentation.start

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.markettwits.start.presentation.membres.StartMembersUi

class MockStartScreen : StartScreen {
    override val start: Value<StartItemUi> = MutableValue(StartItemUi.Loading)
    override fun goBack() = Unit
    override fun goMembers(membersUi: List<StartMembersUi>) = Unit

}