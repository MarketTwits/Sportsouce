package com.markettwits.members.member_add_edit.domain.add

import com.markettwits.members.member_common.domain.ProfileMember

interface MemberAddUseCase {
    suspend fun addMember(member: ProfileMember): Result<Unit>
}