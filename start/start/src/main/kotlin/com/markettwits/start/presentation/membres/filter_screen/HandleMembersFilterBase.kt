package com.markettwits.start.presentation.membres.filter_screen

class HandleMembersFilterBase : HandleMembersFilter {
    override fun changeState(
        currentValue: List<MembersFilterGroup>,
        categoryIndex: Int,
        itemIndex: Int
    ): List<MembersFilterGroup> {
        val currentFilterGroups = currentValue.toMutableList()
        val category = currentFilterGroups[categoryIndex]

        val toggledItems = category.items.mapIndexed { index, filterItem ->
            if (index == itemIndex) {
                when (filterItem) {
                    is MembersFilterItem.Base -> MembersFilterItem.Selected(filterItem.title)
                    is MembersFilterItem.Selected -> MembersFilterItem.Base(filterItem.title)
                }
            } else {
                filterItem
            }
        }
        currentFilterGroups[categoryIndex] = category.copy(items = toggledItems)
        return currentFilterGroups.toList()
    }

    override fun convertToBaseFilter(filterGroup: MembersFilterGroup): MembersFilterGroup {
        val baseItems = filterGroup.items.map {
            when (it) {
                is MembersFilterItem.Selected -> MembersFilterItem.Base(it.title)
                is MembersFilterItem.Base -> it
            }
        }
        return MembersFilterGroup(filterGroup.title, baseItems)
    }
}