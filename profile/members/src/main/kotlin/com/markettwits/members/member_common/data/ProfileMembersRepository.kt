package com.markettwits.members.member_common.data

import com.markettwits.members.member_common.domain.ProfileMember
import kotlinx.coroutines.flow.Flow

interface ProfileMembersRepository {
    suspend fun fetchMembers(): List<ProfileMember>
    suspend fun observeMembers(forced: Boolean = false): Flow<List<ProfileMember>>
    suspend fun deleteMember(memberId: Int): Result<Unit>
    suspend fun updateMember(profileMember: ProfileMember): Result<Unit>
    suspend fun addMember(profileMember: ProfileMember): Result<Unit>
}