package com.markettwits.start.presentation.membres.filter_screen

import com.arkivanov.decompose.value.Value
import com.markettwits.start.presentation.membres.list.StartMembersUi

interface StartMembersFilterScreen {
    val value : Value<List<StartMembersUi>>
    val filter : Value<List<MembersFilterGroup>>
    fun goBack()
    fun reset()
    fun apply()
    fun selectItem(item: MembersFilterItem)
    fun toggleFilterItemState(categoryIndex: Int, itemIndex: Int)
}