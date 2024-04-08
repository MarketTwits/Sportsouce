package com.markettwits.members.member_add_edit.domain.validate

import com.markettwits.members.member_common.domain.ProfileMember

interface AddOrEditMemberValidator {
    fun validateFields(profileMember: ProfileMember): Result<ProfileMember>
}