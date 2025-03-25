package com.markettwits.sportsouce.start.presentation.membres.list.models

import com.markettwits.sportsouce.start.presentation.membres.filter.MembersFilterGroup

interface MembersFilter {
    fun filterByKeyWordTeam(
        value: String,
        initialValue: List<StartMembersUi.Team>
    ): List<StartMembersUi>

    fun filterByKeyWordSingle(
        value: String,
        initialValue: List<StartMembersUi.Single>
    ): List<StartMembersUi>

    fun filterByKeyWord(value: String, initialValue: List<StartMembersUi>): List<StartMembersUi>

    fun filterMembersList(
        membersList: List<StartMembersUi>,
        filters: List<MembersFilterGroup>
    ): List<StartMembersUi>

    fun generateFilterGroups(membersList: List<StartMembersUi>): List<MembersFilterGroup>
}