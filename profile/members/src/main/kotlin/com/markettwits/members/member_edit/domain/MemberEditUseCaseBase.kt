package com.markettwits.members.member_edit.domain

import com.markettwits.members.common.data.ProfileMembersRepository
import com.markettwits.members.common.domain.ProfileMember
import com.markettwits.teams_city.data.TeamsCityRepository
import com.markettwits.teams_city.domain.Team

class MemberEditUseCaseBase(
    private val repository: ProfileMembersRepository,
    private val teamsCityRepository: TeamsCityRepository
) : MemberEditUseCase {
    override suspend fun edit(profileMember: ProfileMember): Result<Unit> =
        repository.updateMember(profileMember)

    override suspend fun teams(): Result<List<Team>> =
        teamsCityRepository.teams()

}