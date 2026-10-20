package com.markettwits.sportsouce.profile.members.member_common.data.mapper

import kotlinx.datetime.number
import com.markettwits.core.time.TimeMapper
import com.markettwits.core.time.TimePattern
import com.markettwits.sportsouce.auth.cloud.model.sign_in.response.User
import com.markettwits.sportsouce.profile.cloud.model.members.ProfileMemberRequest
import com.markettwits.sportsouce.profile.cloud.model.members.ProfileMembers
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import com.markettwits.sportsouce.profile.members.member_common.domain.normalizeMemberPhone
import com.markettwits.sportsouce.profile.members.member_common.domain.memberBirthDate

class MembersMapperBase(private val timeMapper: TimeMapper) : MembersMapper {
    override fun mapAll(members: ProfileMembers): List<ProfileMember> = members.rows.map { mapMember(it) }

    override fun mapMember(member: ProfileMembers.ProfileMember, ownerId: Int): ProfileMember = with(member) {
        ProfileMember(
            id = id, userId = userId ?: ownerId, name = name.orEmpty(), surname = surname.orEmpty(),
            phone = phone.orEmpty(), gender = gender.orEmpty(), team = team.orEmpty(),
            birthday = birthday?.takeIf { it.isNotBlank() }?.let {
                runCatching { memberBirthDate(it).let { date ->
                    "${date.day.toString().padStart(2, '0')}.${date.month.number.toString().padStart(2, '0')}.${date.year}"
                } }.getOrDefault("")
            }.orEmpty(),
            type = type.orEmpty(), email = email.orEmpty(), child = child ?: false,
            identityKnown = true, participantUserId = participantUserId, cityId = cityId,
            vk = vk, instagram = instagram, whatsapp = whatsapp, telegram = telegram,
        )
    }

    override fun addOrUpdate(
        member: ProfileMember,
        update: Boolean,
        userId: Int
    ): ProfileMemberRequest =
        ProfileMemberRequest(
            birthday = memberBirthDate(member.birthday).toString(),
            child = member.child,
            email = member.email.trim().takeIf { !member.child && it.isNotBlank() },
            gender = member.gender,
            id = null,
            name = member.name.trim(),
            phone = if (member.child) "" else normalizeMemberPhone(member.phone),
            surname = member.surname.trim(),
            team = member.team.trim(),
            type = member.type,
            userId = if (update) null else userId.toString()
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
        email = user.email ?: "",
        child = isUserChild(user.age),
        participantUserId = user.id,
        city = user.address.orEmpty(),
        patronymic = user.patronymic,
        source = ProfileMember.Source.Self,
    )

    private fun isUserChild(age: String?): Boolean = age?.toIntOrNull()?.let { it <= 18 } ?: false
}
