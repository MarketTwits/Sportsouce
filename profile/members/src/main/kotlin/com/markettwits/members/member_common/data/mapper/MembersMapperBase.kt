package com.markettwits.members.member_common.data.mapper

import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.profile.members.ProfileMemberRequest
import com.markettwits.cloud.model.profile.members.ProfileMembers
import com.markettwits.core_ui.time.TimeMapper
import com.markettwits.core_ui.time.TimePattern
import com.markettwits.members.member_common.domain.ProfileMember

class MembersMapperBase(private val timeMapper: TimeMapper) : MembersMapper {
    override fun mapAll(members: ProfileMembers): List<ProfileMember> =
        members.rows.map {
            ProfileMember(
                id = it.id,
                userId = it.user_id,
                name = it.name,
                surname = it.surname,
                phone = it.phone,
                gender = it.gender,
                team = it.team,
                birthday = timeMapper.mapTime(TimePattern.FullWithDots, it.birthday),
                type = it.type,
                email = it.email,
                child = it.child ?: false
            )
        }

    override fun addOrUpdate(
        member: ProfileMember,
        update: Boolean,
        userId: Int
    ): ProfileMemberRequest =
        ProfileMemberRequest(
            birthday = timeMapper.mapTimeToCloud(time = member.birthday),
            child = member.child,
            email = member.email,
            gender = member.gender,
            id = if (update) member.id else null,
            name = member.name,
            phone = member.phone,
            surname = member.surname,
            team = member.team,
            type = member.type,
            user_id = userId.toString()
        )

    override fun userToMember(user: User) = ProfileMember(
        id = user.id,
        userId = user.id,
        name = user.name,
        surname = user.surname,
        phone = user.number,
        gender = user.sex,
        team = user.team ?: "",
        birthday = timeMapper.mapTime(TimePattern.FullWithDots, user.birthday),
        type = "",
        email = user.email,
        child = isUserChild(user.age)
    )

    private fun isUserChild(age: String?): Boolean = age?.toIntOrNull()?.let { it <= 18 } ?: false
}