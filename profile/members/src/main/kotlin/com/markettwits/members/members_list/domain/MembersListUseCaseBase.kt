package com.markettwits.members.members_list.domain

import com.markettwits.members.member_common.data.ProfileMembersRepository
import com.markettwits.members.member_common.domain.ProfileMember
import kotlinx.coroutines.flow.Flow

class MembersListUseCaseBase(private val repository: ProfileMembersRepository) :
    MembersListUseCase {
    override suspend fun members(forced: Boolean): Flow<List<ProfileMember>> =
        repository.observeMembers(forced)
}