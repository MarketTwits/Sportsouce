package com.markettwits.start.presentation.member.components

import com.markettwits.members.member_common.domain.ProfileMember
import com.markettwits.start.domain.StartStatement

fun memberSelectApply(
    member: ProfileMember,
    currentState: StartStatement
): StartStatement = currentState.copy(
    name = member.name,
    surname = member.surname,
    birthday = member.birthday,
    sex = member.gender,
    team = member.team,
    email = member.email,
    phone = member.phone
)