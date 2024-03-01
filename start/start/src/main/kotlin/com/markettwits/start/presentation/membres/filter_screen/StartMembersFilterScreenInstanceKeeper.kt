package com.markettwits.start.presentation.membres.filter_screen

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.markettwits.start.presentation.membres.list.StartMembersUi

class StartMembersFilterInstanceKeeper(
    private val membersUi: List<StartMembersUi>
) : InstanceKeeper.Instance {
    val start: MutableValue<List<StartMembersUi>> = MutableValue(emptyList())

    init {
        start.value = membersUi
    }
}