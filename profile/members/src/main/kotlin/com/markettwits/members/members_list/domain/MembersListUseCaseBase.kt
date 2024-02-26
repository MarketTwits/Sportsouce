package com.markettwits.members.members_list.domain

import com.markettwits.members.common.data.ProfileMembersRepository
import com.markettwits.members.common.domain.ProfileMember

class MembersListUseCaseBase(private val repository: ProfileMembersRepository) :
    MembersListUseCase {
    override suspend fun members(forced: Boolean): Result<List<ProfileMember>> =
        repository.fetchMembers(forced)
}