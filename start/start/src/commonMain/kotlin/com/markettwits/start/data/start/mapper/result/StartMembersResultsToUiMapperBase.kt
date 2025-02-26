package com.markettwits.start.data.start.mapper.result

import com.markettwits.start.presentation.result.model.MemberResult
import com.markettwits.start_cloud.model.result.StartMemberResult

internal class StartMembersResultsToUiMapperBase : StartMembersResultsToUiMapper {
    override fun map(membersResult: List<StartMemberResult>): List<MemberResult> = membersResult.map {
        MemberResult(
            id = it.id,
            bodyNumber = it.bodyNumber,
            distance = it.distance,
            circles = it.circles,
            sex = it.sex,
            shift = it.shift,
            result = it.result,
            startId = it.startId,
            team = it.team,
            group = it.group,
            place = it.place ?: 0,
            name = it.name,
        )
    }
}