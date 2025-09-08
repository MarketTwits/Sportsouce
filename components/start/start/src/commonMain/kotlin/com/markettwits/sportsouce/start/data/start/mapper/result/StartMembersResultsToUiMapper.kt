package com.markettwits.sportsouce.start.data.start.mapper.result

import com.markettwits.sportsouce.start.cloud.model.result.v1.StartMemberResultV1
import com.markettwits.sportsouce.start.cloud.model.result.v2.StartMemberResultV2
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult

internal interface StartMembersResultsToUiMapper {

    fun mapV1(membersResult: List<StartMemberResultV1>): List<MemberResult>

    fun mapV2(membersResult: List<StartMemberResultV2>): List<MemberResult>

}