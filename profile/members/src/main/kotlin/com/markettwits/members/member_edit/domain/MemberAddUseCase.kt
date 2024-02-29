package com.markettwits.members.member_edit.domain

import com.markettwits.members.member_common.domain.ProfileMember

interface MemberAddUseCase {
    suspend fun addMember(member: ProfileMember): Result<Unit>
}