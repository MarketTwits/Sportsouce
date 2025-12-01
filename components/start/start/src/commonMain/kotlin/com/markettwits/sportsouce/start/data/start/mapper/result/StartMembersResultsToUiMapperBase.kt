package com.markettwits.sportsouce.start.data.start.mapper.result

import com.markettwits.sportsouce.start.cloud.model.result.v1.StartMemberResultV1
import com.markettwits.sportsouce.start.cloud.model.result.v2.StartMemberResultV2
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult

internal class StartMembersResultsToUiMapperBase : StartMembersResultsToUiMapper {
    override fun mapV1(membersResult: List<StartMemberResultV1>): List<MemberResult> = membersResult.map {
        MemberResult(
            id = it.id,
            bodyNumber = it.bodyNumber,
            distance = it.distance,
            circles = it.circles,
            sex = it.sex,
            shift = it.shift ?: "",
            result = it.result,
            startId = it.startId,
            team = it.team,
            group = it.group,
            place = it.place ?: 0,
            name = it.name,
        )
    }

    override fun mapV2(membersResult: List<StartMemberResultV2>): List<MemberResult> =
        membersResult.mapIndexed { index, memberResult ->
            MemberResult(
                id = memberResult.id,
                bodyNumber = memberResult.num ?: "",
                distance = memberResult.distance ?: "",
                circles = memberResult.checkpointResults.mapIndexed { index, checkpoint ->
                    index + 1 to (checkpoint.displayTime ?: "")
                }.toMap(),
                sex = memberResult.gender ?: "",
                shift = "",
                result = memberResult.result ?: "",
                startId = 0,
                team = memberResult.team ?: "",
                group = memberResult.group ?: "",
                place = memberResult.computedPlace ?: (index + 1),
                name = "${memberResult.name ?: ""} ${memberResult.secondName ?: ""}".trim(),
            )
        }
}