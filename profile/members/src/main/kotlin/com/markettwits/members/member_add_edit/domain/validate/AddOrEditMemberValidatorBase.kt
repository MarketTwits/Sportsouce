package com.markettwits.members.member_add_edit.domain.validate

import com.markettwits.members.member_common.domain.ProfileMember


class AddOrEditMemberValidatorBase : AddOrEditMemberValidatorAbstract() {
    override fun validateFields(profileMember: ProfileMember): Result<ProfileMember> =
        runCatching {
            validateProfileMember(profileMember)
        }
}