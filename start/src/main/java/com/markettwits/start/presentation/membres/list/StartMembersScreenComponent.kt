package com.markettwits.start.presentation.membres.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.markettwits.start.presentation.common.OnClick
import com.markettwits.start.presentation.membres.filter_screen.MembersFilterGroup
import com.markettwits.start.presentation.membres.filter_screen.MembersFilterItem
import kotlinx.serialization.builtins.ListSerializer

class StartMembersScreenComponent(
    componentContext: ComponentContext,
    private val membersUi: List<StartMembersUi>,
    private val openFilterScreen: (List<MembersFilterGroup>) -> Unit,
    private val onBack: OnClick
) : ComponentContext by componentContext, StartMembersScreen {

    override val members: MutableValue<List<StartMembersUi>> = MutableValue(
        stateKeeper.consume(
            key = "MEMBERS_STATE",
            ListSerializer(StartMembersUi.serializer())
        ) ?: membersUi
    )
    private val filterItems: MutableValue<List<MembersFilterGroup>> = MutableValue(
        stateKeeper.consume(
            key = "FILTER_STATE",
            ListSerializer(MembersFilterGroup.serializer())
        ) ?: emptyList()
    )

    fun updateFilter(filter: List<MembersFilterGroup>) {
        filterItems.value = filter
        //TODO CALL SORT FUN
        members.value = filterMembersList(membersUi, filter)
    }

    init {
        //   applyFilterAndSort()

        stateKeeper.register(
            key = "MEMBERS_STATE",
            ListSerializer(StartMembersUi.serializer())
        ) { members.value }
        stateKeeper.register(
            key = "FILTER_STATE",
            ListSerializer(MembersFilterGroup.serializer())
        ) { filterItems.value }
    }

    override fun filter(value: String) {
        if (value.isEmpty()) {
            members.value = membersUi
        } else {
            val filteredMembers = membersUi.filter {
                it.name.contains(
                    value,
                    ignoreCase = true
                ) || it.surname.contains(value, ignoreCase = true)
            }
            val sortedMembers = filteredMembers.sortedWith(compareBy({ it.name }, { it.surname }))
            members.value = sortedMembers
        }
    }

    //    fun filterMembersList(
//        membersList: List<StartMembersUi>,
//        filters: List<MembersFilterGroup>
//    ): List<StartMembersUi> {
//        var filteredList = membersList.toMutableList()
//
//        filters.forEach { filterGroup ->
//            filterGroup.items.forEach { filterItem ->
//                when (filterItem) {
//                    is MembersFilterItem.Selected -> {
//                        filteredList = when (filterGroup.title) {
//                            "Distance" -> filteredList.filter { it.distance == filterItem.title }
//                                .toMutableList()
//
//                            "Team" -> filteredList.filter { it.team == filterItem.title }
//                                .toMutableList()
//
//                            "Group" -> filteredList.filter { it.group == filterItem.title }
//                                .toMutableList()
//
//                            "City" -> filteredList.filter { it.city == filterItem.title }
//                                .toMutableList()
//                            // Add more cases for other filters if needed
//                            else -> filteredList
//                        }
//                    }
//
//                    is MembersFilterItem.Base -> {
//                        // Handle base filter item if needed
//                    }
//                }
//            }
//        }
//
//        return filteredList
//    }
    fun filterMembersList(
        membersList: List<StartMembersUi>,
        filters: List<MembersFilterGroup>
    ): List<StartMembersUi> {
        return membersList.filter { member ->
            filters.filter { it.items.any { filterItem -> filterItem is MembersFilterItem.Selected } }
                .all { filterGroup ->
                    filterGroup.items.any { filterItem ->
                        when (filterItem) {
                            is MembersFilterItem.Selected -> {
                                when (filterGroup.title) {
                                    "Distance" -> member.distance == filterItem.title
                                    "Team" -> member.team == filterItem.title
                                    "Group" -> member.group == filterItem.title
                                    "City" -> member.city == filterItem.title
                                    else -> false
                                }
                            }
                            else -> false
                        }
                    }
                }
        }
    }

    //    fun generateFilterGroups(membersList: List<StartMembersUi>): List<MembersFilterGroup> {
//        val filterGroups = mutableListOf<MembersFilterGroup>()
//
//        // Get distinct values for each property
//        val distanceValues = membersList.map { it.distance }.distinct()
//        val teamValues = membersList.map { it.team }.distinct()
//        val groupValues = membersList.map { it.group }.distinct()
//        val cityValues = membersList.map { it.city }.distinct()
//
//        // Create filter groups dynamically based on available properties
//        if (distanceValues.isNotEmpty()) {
//            val distanceFilterGroup = MembersFilterGroup(
//                title = "Distance",
//                items = distanceValues.map { MembersFilterItem.Base(it) }
//            )
//            filterGroups.add(distanceFilterGroup)
//        }
//
//        if (teamValues.isNotEmpty()) {
//            val teamFilterGroup = MembersFilterGroup(
//                title = "Team",
//                items = teamValues.map { MembersFilterItem.Base(it) }
//            )
//            filterGroups.add(teamFilterGroup)
//        }
//
//        if (groupValues.isNotEmpty()) {
//            val groupFilterGroup = MembersFilterGroup(
//                title = "Group",
//                items = groupValues.map { MembersFilterItem.Base(it) }
//            )
//            filterGroups.add(groupFilterGroup)
//        }
//
//        if (cityValues.isNotEmpty()) {
//            val cityFilterGroup = MembersFilterGroup(
//                title = "City",
//                items = cityValues.map { MembersFilterItem.Base(it) }
//            )
//            filterGroups.add(cityFilterGroup)
//        }
//
//        return filterGroups
//    }
    fun generateFilterGroups(membersList: List<StartMembersUi>): List<MembersFilterGroup> {
        val filterGroups = mutableListOf<MembersFilterGroup>()

        val propertyNames = listOf("Distance", "Team", "Group", "City")

        for (propertyName in propertyNames) {
            val distinctValues = membersList.mapNotNull {
                when (propertyName) {
                    "Distance" -> it.distance
                    "Team" -> it.team
                    "Group" -> it.group
                    "City" -> it.city
                    else -> null
                }
            }.distinct()

            if (distinctValues.isNotEmpty()) {
                val filterGroup = MembersFilterGroup(
                    title = propertyName,
                    items = distinctValues.map { MembersFilterItem.Base(it) }
                )
                filterGroups.add(filterGroup)
            }
        }

        return filterGroups
    }

    override fun openFilter() {
        // updateFilterGroups()
        if (filterItems.value.isEmpty()) {
            filterItems.value =
                generateFilterGroups(membersUi)
        }
        openFilterScreen(filterItems.value)
    }

    override fun back() {
        onBack()
    }


}