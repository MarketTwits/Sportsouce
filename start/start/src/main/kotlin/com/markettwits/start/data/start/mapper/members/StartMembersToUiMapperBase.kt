package com.markettwits.start.data.start.mapper.members

import com.markettwits.cloud.model.start_member.StartMemberItem
import com.markettwits.start.presentation.membres.list.StartMembersUi

internal class StartMembersToUiMapperBase : StartMembersToUiMapper {

    override fun map(
        startMember: List<StartMemberItem>,
        paymentDisabled: Boolean
    ): List<StartMembersUi> =
        convertToStartMembersUiNew(startMember, paymentDisabled)


    private fun convertToStartMembersUiNew(
        startMember: List<StartMemberItem>,
        paymentDisabled: Boolean
    ): List<StartMembersUi> {

        val teamMap = mutableMapOf<String, MutableList<StartMemberItem>>()


        for (item in startMember) {
            val safeRegCode = item.reg_code ?: ""
            if (item.payment != null && safeRegCode.isNotEmpty()) {
                teamMap.computeIfAbsent(safeRegCode) { mutableListOf() }.add(item)
            }
        }
        return if (teamMap.isEmpty()) {
            val mapSingleMembers = if (paymentDisabled) {
                startMember.map { convertToSingle(it) }
            } else {
                startMember
                    .filter { it.payment != null }
                    .map { convertToSingle(it) }
            }
            mapSingleMembers
        } else {
            val teams = teamMap
                .values
                .filter { it.size > 1 } // Filter only items with duplicate reg_code
                .map { createTeam(it) }

            val singles = startMember
                .filter { it.payment != null && (it.reg_code.isNullOrEmpty()) }
                .map { convertToSingle(it) }

            singles + teams
        }
    }

    private fun createTeam(teamMembers: List<StartMemberItem>): StartMembersUi.Team {
        val teamMemberList = teamMembers.map {
            StartMembersUi.TeamMember(
                memberId = it.teamNumber ?: 0,
                name = it.name,
                surname = it.surname
            )
        }

        return StartMembersUi.Team(
            members = teamMemberList,
            distance = teamMembers[0].distance,
            team = teamMembers[0].team,
            group = teamMembers[0].mapStartMember(teamMembers[0].group ?: "").name,
            city = teamMembers[0].city ?: ""
        )
    }

    private fun convertToSingle(startMemberItem: StartMemberItem): StartMembersUi.Single {
        return StartMembersUi.Single(
            id = startMemberItem.id,
            name = startMemberItem.name,
            surname = startMemberItem.surname,
            distance = startMemberItem.distance,
            team = startMemberItem.team,
            group = startMemberItem.mapStartMember(startMemberItem.group ?: "").name,
            city = startMemberItem.city ?: ""
        )
    }
}