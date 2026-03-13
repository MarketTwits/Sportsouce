package com.markettwits.sportsouce.start.register.presentation.registration.member.domain

import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import com.markettwits.sportsouce.start.register.domain.StartStatement

internal fun StartStatement.isPresentInProfileMembers(
    members: List<ProfileMember>,
    emailOverride: String? = null,
    phoneOverride: String? = null,
): Boolean {
    val targetName = name.trim().lowercase()
    val targetSurname = surname.trim().lowercase()
    val targetEmail = (emailOverride ?: email).trim().lowercase()
    val targetPhone = normalizePhone(phoneOverride ?: phone)

    return members.any { member ->
        member.name.trim().lowercase() == targetName &&
                member.surname.trim().lowercase() == targetSurname &&
                member.email.trim().lowercase() == targetEmail &&
                normalizePhone(member.phone) == targetPhone
    }
}

private fun normalizePhone(value: String): String = value.filter { it.isDigit() }
