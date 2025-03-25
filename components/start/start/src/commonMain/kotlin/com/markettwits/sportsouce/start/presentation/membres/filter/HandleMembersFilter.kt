package com.markettwits.sportsouce.start.presentation.membres.filter

interface HandleMembersFilter {

    fun changeState(
        currentValue: List<MembersFilterGroup>,
        categoryIndex: Int,
        itemIndex: Int
    ): List<MembersFilterGroup>

    fun convertToBaseFilter(filterGroup: MembersFilterGroup): MembersFilterGroup
}