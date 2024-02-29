package com.markettwits.members.members_list.domain

import com.markettwits.members.member_common.data.ProfileMembersRepository
import com.markettwits.members.member_common.domain.ProfileMember

class MembersListUseCaseBase(private val repository: ProfileMembersRepository) :
    MembersListUseCase {
    override suspend fun members(forced: Boolean): Result<List<ProfileMember>> =
        repository.fetchMembers(forced)
}