package com.markettwits.start.presentation.start

import com.arkivanov.decompose.value.Value
import com.markettwits.start.presentation.membres.StartMembersUi

interface StartScreen {
    val start: Value<StartItemUi>
    fun goBack()
    fun goMembers(membersUi: List<StartMembersUi>)
}