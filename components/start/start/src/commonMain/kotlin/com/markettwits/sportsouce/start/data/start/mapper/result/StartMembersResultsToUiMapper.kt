package com.markettwits.sportsouce.start.data.start.mapper.result

import com.markettwits.sportsouce.start.cloud.model.result.StartMemberResult
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult

internal interface StartMembersResultsToUiMapper {

    fun map(membersResult: List<StartMemberResult>): List<MemberResult>

}