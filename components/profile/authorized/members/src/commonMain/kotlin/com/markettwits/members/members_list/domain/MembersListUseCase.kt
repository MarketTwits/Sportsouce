package com.markettwits.members.members_list.domain

import com.markettwits.members.member_common.domain.ProfileMember
import kotlinx.coroutines.flow.Flow

interface MembersListUseCase {
    suspend fun members(forced: Boolean): Flow<List<ProfileMember>>
}