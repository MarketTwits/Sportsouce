package com.markettwits.sportsouce.start.register.presentation.registration.member.component

import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import com.markettwits.sportsouce.start.register.domain.StartStatement
import kotlinx.serialization.Serializable

@Serializable
data class RegistrationMemberInput(
    val startStatement: StartStatement,
    val membersProfile: List<ProfileMember>,
    val memberId: Int,
)