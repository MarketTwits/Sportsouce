package com.markettwits.members.member_add.domain

import com.markettwits.members.common.domain.ProfileMember

interface MemberAddUseCase {
    suspend fun addMember(member: ProfileMember): Result<Unit>
}