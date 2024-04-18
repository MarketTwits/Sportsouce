package com.markettwits.start.presentation.membres.filter_screen

interface HandleMembersFilter {
    fun changeState(
        currentValue: List<MembersFilterGroup>,
        categoryIndex: Int,
        itemIndex: Int
    ): List<MembersFilterGroup>

    fun convertToBaseFilter(filterGroup: MembersFilterGroup): MembersFilterGroup
}