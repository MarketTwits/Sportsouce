package com.markettwits.members.member_edit.domain

import com.markettwits.members.member_common.data.ProfileMembersRepository
import com.markettwits.members.member_common.domain.ProfileMember

class MemberAddUseCaseBase(private val repository: ProfileMembersRepository) : MemberAddUseCase {
    override suspend fun addMember(member: ProfileMember): Result<Unit> =
        repository.addMember(member)
}