package com.markettwits.start.data

import com.markettwits.cloud.model.start_member.StartMemberItem
import com.markettwits.start.presentation.membres.list.StartMembersUi
import java.util.TreeMap

interface StartMembersToUiMapper {
    fun map(startMember: List<StartMemberItem>): List<StartMembersUi>
    fun maps(startMember: List<StartMemberItem>): List<StartMembersUi>

    class Base : StartMembersToUiMapper {
        override fun map(startMember: List<StartMemberItem>): List<StartMembersUi> {
            val list = mutableListOf<StartMembersUi>()
            startMember.forEach {
                if (it.payment != null) {
                    list.add(
                        StartMembersUi.Single(
                            id = it.id,
                            name = it.name,
                            surname = it.surname,
                            distance = it.distance,
                            team = it.team,
                            group = it.mapStartMember(it.group).name,
                            city = it.city ?: ""
                        )
                    )
                }
            }
            return list
        }

        override fun maps(startMember: List<StartMemberItem>): List<StartMembersUi> {
            val data = convertToStartMembersUiNew(startMember)
            return data
        }

        fun convertToStartMembersUi(startMember: List<StartMemberItem>): List<StartMembersUi> {
            val teamMap = mutableMapOf<String, MutableList<StartMemberItem>>()

            for (item in startMember) {
                if (item.payment != null) {
                    val order = item.reg_code
                    if (order.isNotEmpty()) {
                        teamMap.computeIfAbsent(order) { mutableListOf() }.add(item)
                    }

                }
            }

            val result = mutableListOf<StartMembersUi>()
            for ((_, teamMembers) in teamMap) {
                if (teamMembers.size > 1) {
                    // Create a team if there are duplicate order_numbers
                    val team = createTeam(teamMembers)
                    result.add(team)
                } else {
                    // Convert single member to StartMembersUi.Single
                    val single = convertToSingle(teamMembers[0])
                    result.add(single)
                }
            }
            return result
        }

        @Deprecated("dd")
        private fun convertToStartMembersUiNew(startMember: List<StartMemberItem>): List<StartMembersUi> {

            val teamMap = mutableMapOf<String, MutableList<StartMemberItem>>()

            // Group items by reg_code
            for (item in startMember) {
                if (item.payment != null && item.reg_code.isNotEmpty()) {
                    val regCode = item.reg_code
                    teamMap.computeIfAbsent(regCode) { mutableListOf() }.add(item)
                }
            }

            return if (teamMap.isEmpty()) {
                startMember
                    .filter { it.payment != null }
                    .map { convertToSingle(it) }
            } else {
                val teams = teamMap
                    .values
                    .filter { it.size > 1 } // Filter only items with duplicate reg_code
                    .map { createTeam(it) }

                val singles = startMember
                    .filter { it.payment != null && (it.reg_code == null || it.reg_code.isEmpty()) }
                    .map { convertToSingle(it) }

                singles + teams
            }

        }

        private fun createTeam(teamMembers: List<StartMemberItem>): StartMembersUi.Team {
            val teamMemberList = teamMembers.map {
                StartMembersUi.TeamMember(
                    memberId = it.teamNumber,
                    name = it.name,
                    surname = it.surname
                )
            }

            return StartMembersUi.Team(
                members = teamMemberList,
                distance = teamMembers[0].distance,
                team = teamMembers[0].team,
                group = teamMembers[0].mapStartMember(teamMembers[0].group).name,
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
                group = startMemberItem.mapStartMember(startMemberItem.group).name,
                city = startMemberItem.city ?: ""
            )
        }

//        private fun mapToUi(teamMembers: List<StartMemberItem>): List<StartMembersUi> {
//            // Implement logic to map team members to UI representation
//            // This is a placeholder, you may need to adjust it based on your requirements
//            return teamMembers.map {
//                StartMembersUi(
//                    id = it.id,
//                    name = it.name,
//                    surname = it.surname,
//                    distance = it.distance,
//                    team = it.team,
//                    group = it.mapStartMember(it.group).name,
//                    city = it.city ?: ""
//                )
//            }
//        }
    }
}