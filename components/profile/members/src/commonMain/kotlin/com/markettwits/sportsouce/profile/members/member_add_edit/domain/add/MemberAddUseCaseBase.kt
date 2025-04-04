package com.markettwits.sportsouce.profile.members.member_add_edit.domain.add

import com.markettwits.core_ui.items.extensions.flatMapCallback
import com.markettwits.sportsouce.profile.members.member_add_edit.domain.validate.AddOrEditMemberValidator
import com.markettwits.sportsouce.profile.members.member_common.data.ProfileMembersRepository
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember

class MemberAddUseCaseBase(
    private val repository: ProfileMembersRepository,
    private val addOrEditMemberValidator: AddOrEditMemberValidator
) : MemberAddUseCase {
    override suspend fun addMember(member: ProfileMember): Result<Unit> =
        addOrEditMemberValidator.validateFields(member).flatMapCallback {
            repository.addMember(it)
        }
}