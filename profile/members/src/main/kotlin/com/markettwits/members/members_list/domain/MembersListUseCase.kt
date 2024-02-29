package com.markettwits.members.members_list.domain

import com.markettwits.members.member_common.domain.ProfileMember

interface MembersListUseCase {
    suspend fun members(forced: Boolean): Result<List<ProfileMember>>
}