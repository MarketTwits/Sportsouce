package com.markettwits.sportsouce.profile.members.member_common.data.mapper

import com.markettwits.sportsouce.auth.cloud.model.sign_in.response.User
import com.markettwits.sportsouce.profile.cloud.model.members.ProfileMemberRequest
import com.markettwits.sportsouce.profile.cloud.model.members.ProfileMembers
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember

interface MembersMapper {
    fun mapAll(members: ProfileMembers): List<ProfileMember>
    fun addOrUpdate(member: ProfileMember, update: Boolean, userId: Int): ProfileMemberRequest
    fun userToMember(user: User): ProfileMember
}