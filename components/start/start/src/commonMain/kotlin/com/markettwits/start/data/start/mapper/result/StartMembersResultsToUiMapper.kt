package com.markettwits.start.data.start.mapper.result

import com.markettwits.start.presentation.result.model.MemberResult
import com.markettwits.start_cloud.model.result.StartMemberResult

internal interface StartMembersResultsToUiMapper {

    fun map(membersResult: List<StartMemberResult>): List<MemberResult>

}