package com.markettwits.sportsouce.start.register.presentation.registration.member.domain

import com.markettwits.sportsouce.profile.members.member_common.data.ProfileMembersRepository
import com.markettwits.sportsouce.profile.members.member_common.domain.MemberIdentityRepository
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/** Fresh permissions are loaded separately from the cached manual participant templates. */
class RegistrationParticipantsRepository(
    private val profiles: ProfileMembersRepository,
    private val identity: MemberIdentityRepository,
) {
    suspend fun load(): Result<List<ProfileMember>> = runCatching {
        coroutineScope {
            val saved = async { profiles.fetchMembers(withUser = true) }
            val team = async { identity.team().getOrThrow() }
            mergeRegistrationParticipants(saved.await(), team.await().myTeam.mapNotNull { it.registrationMember })
        }
    }
}

internal fun mergeRegistrationParticipants(saved: List<ProfileMember>, team: List<ProfileMember>): List<ProfileMember> {
    val self = saved.filter { it.source == ProfileMember.Source.Self }
    val selfIds = self.mapNotNull { it.participantUserId }.toSet()
    // A pending request or a reverse permission (trusted) does not grant registration rights.
    val confirmed = team.filter { it.source == ProfileMember.Source.Team && (it.participantUserId ?: 0) > 0 }
        .filterNot { it.participantUserId in selfIds }.distinctBy { it.participantUserId }
    val linkedIds = (self + confirmed).mapNotNull { it.participantUserId }.toSet()
    return self + confirmed + saved.filter { it.source == ProfileMember.Source.Saved && it.participantUserId !in linkedIds }
}
