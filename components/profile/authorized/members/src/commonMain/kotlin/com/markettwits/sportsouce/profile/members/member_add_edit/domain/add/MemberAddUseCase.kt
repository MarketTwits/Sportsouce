package com.markettwits.sportsouce.profile.members.member_add_edit.domain.add

import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember

interface MemberAddUseCase {
    suspend fun addMember(member: ProfileMember): Result<Unit>
}