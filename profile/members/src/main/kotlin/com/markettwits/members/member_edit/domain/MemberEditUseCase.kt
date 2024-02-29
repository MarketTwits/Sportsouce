package com.markettwits.members.member_edit.domain

import com.markettwits.members.member_common.domain.ProfileMember
import com.markettwits.teams_city.domain.Team

interface MemberEditUseCase {
    suspend fun edit(profileMember: ProfileMember): Result<Unit>
    suspend fun teams(): Result<List<Team>>
}