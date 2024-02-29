package com.markettwits.members.member_common.data

import com.markettwits.members.member_common.domain.ProfileMember

interface ProfileMembersRepository {
    suspend fun fetchMembers(forced: Boolean = false): Result<List<ProfileMember>>
    suspend fun deleteMember(memberId: Int): Result<Unit>
    suspend fun updateMember(profileMember: ProfileMember): Result<Unit>
    suspend fun addMember(profileMember: ProfileMember): Result<Unit>
}