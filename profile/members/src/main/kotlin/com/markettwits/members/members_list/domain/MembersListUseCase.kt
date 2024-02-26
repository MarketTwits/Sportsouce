package com.markettwits.members.members_list.domain

import com.markettwits.members.common.domain.ProfileMember

interface MembersListUseCase {
    suspend fun members(forced: Boolean): Result<List<ProfileMember>>
}