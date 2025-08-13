package com.markettwits.sportsouce.start.data.start.mapper.members

import com.markettwits.sportsouce.start.cloud.model.members.StartMember
import com.markettwits.sportsouce.start.presentation.membres.models.StartMembersUi

class StartMembersNewToUiMapper {

    fun map(member: StartMember): StartMembersUi {
        return if (member.members.size < 2) {
            val member = member.members.first()
            StartMembersUi.Single(
                id = member.id,
                name = member.name,
                surname = member.surname,
                distance = member.distanceRelation.name,
                team = member.team,
                group = member.ageGroup?.name ?: "",
                city = member.city
            )
        } else {
            StartMembersUi.Team(
                members = member.members.map { member ->
                    StartMembersUi.TeamMember(
                        memberId = member.id,
                        name = member.name,
                        surname = member.surname
                    )
                },
                distance = member.members.firstOrNull()?.distanceRelation?.name ?: "",
                team = member.members.firstOrNull()?.team ?: "",
                group = member.members.firstOrNull()?.ageGroup?.name ?: "",
                city = member.members.firstOrNull()?.city ?: ""
            )
        }
    }

    fun map(members: List<StartMember>): List<StartMembersUi> =
        members.map { item ->
            map(item)
        }
}