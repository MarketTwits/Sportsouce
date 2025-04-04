package com.markettwits.sportsouce.profile.members.member_detail.domain

import com.markettwits.sportsouce.profile.members.member_common.data.ProfileMembersRepository

class MemberDetailUseCaseBase(private val repository: ProfileMembersRepository) :
    MemberDetailUseCase {
    override suspend fun deleteMember(memberId: Int): Result<Unit> =
        repository.deleteMember(memberId)
}