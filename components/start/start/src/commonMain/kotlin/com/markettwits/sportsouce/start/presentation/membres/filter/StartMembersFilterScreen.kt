package com.markettwits.sportsouce.start.presentation.membres.filter

import com.arkivanov.decompose.value.Value

interface StartMembersFilterScreen {

    val filter: Value<List<MembersFilterGroup>>

    fun goBack()

    fun reset()

    fun apply()

    fun toggleFilterItemState(categoryIndex: Int, itemIndex: Int)
}