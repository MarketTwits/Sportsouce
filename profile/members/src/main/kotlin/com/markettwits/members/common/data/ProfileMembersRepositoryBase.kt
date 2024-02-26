package com.markettwits.members.common.data

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.members.common.data.mapper.MembersMapper
import com.markettwits.members.common.domain.ProfileMember
import com.markettwits.profile.data.AuthDataSource

class ProfileMembersRepositoryBase(
    private val service: SportsouceApi,
    private val auth: AuthDataSource,
    private val mapper: MembersMapper
) : ProfileMembersRepository {

    override suspend fun fetchMembers(forced: Boolean): Result<List<ProfileMember>> = runCatching {
        val user = auth.auth().getOrThrow()
        val token = auth.updateToken().getOrThrow()
        val members = service.memberTemplate(user.id, token)
        mapper.mapAll(members)
    }

    override suspend fun deleteMember(memberId: Int): Result<Unit> = runCatching {
        val token = auth.updateToken().getOrThrow()
        service.deleteMember(memberId, token)
    }

    override suspend fun updateMember(profileMember: ProfileMember): Result<Unit> = runCatching {
        val token = auth.updateToken().getOrThrow()
        val request = mapper.addOrUpdate(profileMember, true)
        service.updateMember(profileMember.id, request, token)
    }

    override suspend fun addMember(profileMember: ProfileMember): Result<Unit> = runCatching {
        val token = auth.updateToken().getOrThrow()
        val request = mapper.addOrUpdate(profileMember, false)
        service.addMember(request, token)
    }
}