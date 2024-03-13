package com.markettwits.start.presentation.membres.list.filter

import com.markettwits.start.presentation.membres.filter_screen.MembersFilterGroup
import com.markettwits.start.presentation.membres.filter_screen.MembersFilterItem
import com.markettwits.start.presentation.membres.list.StartMembersUi

class MembersFilterBase : MembersFilter {
    override fun filterByKeyWordSingle(
        value: String,
        initialValue: List<StartMembersUi.Single>
    ): List<StartMembersUi> {
        return if (value.isEmpty()) {
            initialValue
        } else {
            val filteredMembers = initialValue.filter {
                it.name.contains(
                    value,
                    ignoreCase = true
                ) || it.surname.contains(value, ignoreCase = true)
            }
            filteredMembers.sortedWith(compareBy({ it.name }, { it.surname }))
        }
    }

    override fun filterByKeyWord(
        value: String,
        initialValue: List<StartMembersUi>
    ): List<StartMembersUi> {
        return if (value.isEmpty()) {
            initialValue
        } else {
            val filteredList = initialValue.flatMap { member ->
                when (member) {
                    is StartMembersUi.Single -> filterByKeyWordSingle(value, listOf(member))
                    is StartMembersUi.Team -> filterByKeyWordTeam(value, listOf(member))
                }
            }
            filteredList
        }
    }

    override fun filterByKeyWordTeam(
        value: String,
        initialValue: List<StartMembersUi.Team>
    ): List<StartMembersUi> {
        return if (value.isEmpty()) {
            initialValue
        } else {
            val filteredMembers = initialValue.filter { team ->
                team.members.any { member ->
                    member.name.contains(value, ignoreCase = true) ||
                            member.surname.contains(value, ignoreCase = true)
                }
            }

            filteredMembers.sortedWith(compareBy({ it.team }, { it.distance }))
        }
    }

    override fun filterMembersList(
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
                                    "Дистанция" -> member.distance == filterItem.title
                                    "Команда" -> member.team == filterItem.title
                                    "Группа" -> member.group == filterItem.title
                                    "Город" -> member.city == filterItem.title
                                    else -> false
                                }
                            }
                            else -> false
                        }
                    }
                }
        }
    }

    override fun generateFilterGroups(membersList: List<StartMembersUi>): List<MembersFilterGroup> {
        val filterGroups = mutableListOf<MembersFilterGroup>()
        val propertyNames = listOf("Дистанция", "Команда", "Группа", "Город")
        for (propertyName in propertyNames) {
            val distinctValues = membersList.mapNotNull {
                when (propertyName) {
                    "Дистанция" -> it.distance
                    "Команда" -> it.team
                    "Группа" -> it.group
                    "Город" -> it.city
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
}