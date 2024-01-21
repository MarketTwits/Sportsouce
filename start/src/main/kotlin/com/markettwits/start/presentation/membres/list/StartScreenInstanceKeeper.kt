package com.markettwits.start.presentation.membres.list

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.markettwits.start.presentation.membres.filter_screen.MembersFilterGroup
import com.markettwits.start.presentation.membres.filter_screen.MembersFilterItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class StartMembersScreenInstanceKeeper(
    private val membersUi: List<StartMembersUi>,
    private val filter: MutableValue<List<MembersFilterGroup>>
) : InstanceKeeper.Instance {
    private val scope = CoroutineScope(Dispatchers.Main)
    val start: MutableValue<List<StartMembersUi>> = MutableValue(emptyList())
    init{
        start.value = membersUi

        filter.subscribe {
            applyFilterAndSort()
        }

    }
    fun filter(value: String) {
        if (value.isEmpty()) {
            start.value = membersUi
        } else {
//            val filteredMembers = membersUi.filter {
//                it.name.contains(
//                    value,
//                    ignoreCase = true
//                ) || it.surname.contains(value, ignoreCase = true)
//            }
           // val sortedMembers = filteredMembers.sortedWith(compareBy({ it.name }, { it.surname }))
            //start.value = sortedMembers
        }
    }

    private fun applyFilterAndSort() {
        val selectedCategories = filter.value
            .filterIsInstance<MembersFilterItem.Selected>()
            .map { it.title }

        if (selectedCategories.isEmpty()) {
            // No selected categories, show all members
            start.value = membersUi
        } else {
            // Filter members based on selected categories
//            val filteredMembers = membersUi.filter { member ->
//                selectedCategories.any { category ->
//                    member.name.contains(category, ignoreCase = true) ||
//                            member.surname.contains(category, ignoreCase = true)
//                }
//            }
            // Sort the filtered members
//            val sortedMembers = filteredMembers.sortedWith(compareBy({ it.name }, { it.surname }))
//            start.value = sortedMembers
        }
    }
}