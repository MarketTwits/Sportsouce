package com.markettwits.members.member_add_edit.domain.edit


import com.markettwits.core_ui.items.extensions.flatMapCallback
import com.markettwits.members.member_add_edit.domain.validate.AddOrEditMemberValidator
import com.markettwits.members.member_common.data.ProfileMembersRepository
import com.markettwits.members.member_common.domain.ProfileMember
import com.markettwits.teams_city.data.TeamsCityRepository
import com.markettwits.teams_city.domain.Team

class MemberEditUseCaseBase(
    private val profileRepository: ProfileMembersRepository,
    private val teamsCityRepository: TeamsCityRepository,
    private val addOrEditMemberValidator: AddOrEditMemberValidator
) : MemberEditUseCase {
    override suspend fun edit(profileMember: ProfileMember): Result<Unit> =
        addOrEditMemberValidator.validateFields(profileMember).flatMapCallback {
            profileRepository.updateMember(it)
        }


    override suspend fun teams(): Result<List<Team>> =
        teamsCityRepository.teams()

}