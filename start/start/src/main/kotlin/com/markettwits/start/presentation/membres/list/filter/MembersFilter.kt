package com.markettwits.start.presentation.membres.list.filter

import com.markettwits.start.presentation.membres.filter_screen.MembersFilterGroup
import com.markettwits.start.presentation.membres.list.StartMembersUi

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