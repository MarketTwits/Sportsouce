package com.markettwits.sportsouce.start.data.start.mapper.result

import com.markettwits.sportsouce.start.cloud.model.result.StartMemberResult
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult

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