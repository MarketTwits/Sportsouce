package com.markettwits.sportsouce.start.register.presentation.registration.member.components

import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import com.markettwits.sportsouce.start.register.domain.StartStatement

fun memberSelectApply(member: ProfileMember, currentState: StartStatement): StartStatement = currentState.copy(
    name = member.name,
    surname = member.surname,
    birthday = member.birthday,
    sex = member.gender,
    city = member.city,
    team = member.team,
    email = member.email,
    phone = member.phone,
    patronymic = member.patronymic,
    participantUserId = member.participantUserId,
    participantSource = member.source,
    // userId on a saved template identifies its owner, not the participant.
    userId = if (member.source == ProfileMember.Source.Self) StartStatement.UserId.WithId(member.userId.toString())
        else StartStatement.UserId.WithoutId,
)

internal fun ProfileMember.registrationLabel(): String = "${surname.trim()} ${name.trim()}".trim() + when (source) {
    ProfileMember.Source.Self -> " (я)"
    ProfileMember.Source.Team -> " (команда)"
    ProfileMember.Source.Saved -> ""
}

internal fun StartStatement.withIdentityDetachedIfChanged(previous: StartStatement): StartStatement {
    fun String.normalized() = trim().lowercase().replace('ё', 'е')
    val changed = name.normalized() != previous.name.normalized() || surname.normalized() != previous.surname.normalized() ||
        birthday != previous.birthday || sex != previous.sex
    return if (changed) copy(participantUserId = null, participantSource = ProfileMember.Source.Saved,
        userId = StartStatement.UserId.WithoutId) else this
}
