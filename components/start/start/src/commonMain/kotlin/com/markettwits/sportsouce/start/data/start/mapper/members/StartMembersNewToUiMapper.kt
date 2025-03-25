package com.markettwits.sportsouce.start.data.start.mapper.members

import com.markettwits.sportsouce.start.cloud.model.members.StartMember
import com.markettwits.sportsouce.start.presentation.membres.list.models.StartMembersUi

class StartMembersNewToUiMapper {
    fun map(members : List<StartMember>) : List<StartMembersUi>{
        return members.map {
            if(it.members.size < 2){
                val member = it.members.first()
                StartMembersUi.Single(
                    id = member.id,
                    name = member.name,
                    surname = member.surname,
                    distance = member.distance_relation.name,
                    team = member.team,
                    group = member.age_group?.name ?: "",
                    city = member.city
                )
            }else{
                StartMembersUi.Team(
                    members = it.members.map { member ->
                        StartMembersUi.TeamMember(
                            memberId = member.id,
                            name = member.name,
                            surname = member.surname
                        )
                    },
                    distance = it.members.firstOrNull()?.distance_relation?.name ?: "",
                    team = it.members.firstOrNull()?.team ?: "",
                    group = it.members.firstOrNull()?.age_group?.name ?: "",
                    city = it.members.firstOrNull()?.city ?: ""
                )
            }
        }
    }
}