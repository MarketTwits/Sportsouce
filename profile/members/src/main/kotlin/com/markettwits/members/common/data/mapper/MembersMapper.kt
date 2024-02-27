package com.markettwits.members.common.data.mapper

import com.markettwits.cloud.model.profile.members.ProfileMemberRequest
import com.markettwits.cloud.model.profile.members.ProfileMembers
import com.markettwits.members.common.domain.ProfileMember

interface MembersMapper {
    fun mapAll(members: ProfileMembers): List<ProfileMember>
    fun addOrUpdate(member: ProfileMember, update: Boolean, userId: Int): ProfileMemberRequest
}