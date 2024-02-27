package com.markettwits.members.member_add.domain

import com.markettwits.members.common.data.ProfileMembersRepository
import com.markettwits.members.common.domain.ProfileMember

class MemberAddUseCaseBase(private val repository: ProfileMembersRepository) : MemberAddUseCase {
    override suspend fun addMember(member: ProfileMember): Result<Unit> =
        repository.addMember(member)
}