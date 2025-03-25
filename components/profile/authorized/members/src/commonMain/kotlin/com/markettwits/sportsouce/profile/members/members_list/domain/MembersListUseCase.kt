package com.markettwits.sportsouce.profile.members.members_list.domain

import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import kotlinx.coroutines.flow.Flow

interface MembersListUseCase {
    suspend fun members(forced: Boolean): Flow<List<ProfileMember>>
}