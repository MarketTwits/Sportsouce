package com.markettwits.sportsouce.profile.members.member_add_edit.domain.edit

import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import com.markettwits.sportsouce.teams_city.domain.Team

interface MemberEditUseCase {
    suspend fun edit(profileMember: ProfileMember): Result<Unit>
    suspend fun teams(): Result<List<Team>>
}