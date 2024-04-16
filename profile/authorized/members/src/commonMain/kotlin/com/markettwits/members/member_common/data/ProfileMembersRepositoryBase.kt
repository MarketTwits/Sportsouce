package com.markettwits.members.member_common.data

import com.markettwits.cahce.Cache
import com.markettwits.cahce.execute.list.ExecuteListWithCache
import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.members.member_common.data.mapper.MembersMapper
import com.markettwits.members.member_common.domain.ProfileMember
import com.markettwits.profile.api.AuthDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProfileMembersRepositoryBase(
    private val service: SportsouceApi,
    private val auth: AuthDataSource,
    private val mapper: MembersMapper,
    private val cache: Cache<List<ProfileMember>>,
    private val executeWithCache: ExecuteListWithCache
) : ProfileMembersRepository {

    override suspend fun observeMembers(
        forced: Boolean,
        withUser: Boolean
    ): Flow<List<ProfileMember>> = flow {
        executeWithCache.executeListWithCache(
            forced = forced,
            cache = cache,
            launch = ::fetchMembers,
            callback = {
                emit(obtainMembersCallback(withUser, it))
            }
        )
    }

    private suspend fun obtainMembersCallback(
        withUser: Boolean,
        items: List<ProfileMember>
    ): List<ProfileMember> =
        if (withUser) items.toMutableList().apply { add(userAsMember().getOrThrow()) } else items

    override suspend fun fetchMembers(withUser: Boolean): List<ProfileMember> {
        val user = auth.auth().getOrThrow()
        val token = auth.updateToken().getOrThrow()
        val members = service.memberTemplate(user.id, token)
        val mapMembers = mapper.mapAll(members).toMutableList()
        if (withUser)
            mapMembers.add(userAsMember().getOrThrow())
        return mapMembers
    }


    override suspend fun deleteMember(memberId: Int): Result<Unit> = runCatching {
        val token = auth.updateToken().getOrThrow()
        service.deleteMember(memberId, token)
    }

    override suspend fun updateMember(profileMember: ProfileMember): Result<Unit> = runCatching {
        val token = auth.updateToken().getOrThrow()
        val user = auth.user().getOrThrow()
        val request = mapper.addOrUpdate(profileMember, true, user.id)
        service.updateMember(profileMember.id, request, token)
    }

    override suspend fun addMember(profileMember: ProfileMember): Result<Unit> = runCatching {
        val token = auth.updateToken().getOrThrow()
        val user = auth.user().getOrThrow()
        val request = mapper.addOrUpdate(profileMember, false, user.id)
        service.addMember(request, token)
    }

    override suspend fun userAsMember(): Result<ProfileMember> = runCatching {
        mapper.userToMember(auth.user().getOrThrow())
    }
}