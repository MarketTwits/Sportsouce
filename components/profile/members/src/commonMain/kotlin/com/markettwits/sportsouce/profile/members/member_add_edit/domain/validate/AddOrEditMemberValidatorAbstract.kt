package com.markettwits.sportsouce.profile.members.member_add_edit.domain.validate


import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember

abstract class AddOrEditMemberValidatorAbstract : AddOrEditMemberValidator {

    protected fun validateProfileMember(profileMember: ProfileMember): ProfileMember {
        if (profileMember.name.isEmpty()) throw IllegalArgumentException(
            "Имя не должно быть пустым"
        )
        if (profileMember.surname.isEmpty()) throw IllegalArgumentException(
            "Фамилия не должно быть пустой"
        )
        if (validateRussianPhoneNumber(profileMember.phone)) throw IllegalArgumentException(
            "Введите корректый номер телефона"
        )
        if (validateEmail(profileMember.email)
        ) throw IllegalArgumentException(
            "Введите корректую почту"
        )
        if (profileMember.birthday.isEmpty()) throw IllegalArgumentException(
            "День рождения не должен быть пустой"
        )
        if (profileMember.gender.isEmpty()) throw IllegalArgumentException(
            "Пол не должен быть пустым"
        )
        if (profileMember.team.isEmpty()) throw IllegalArgumentException(
            "Введите корректное название команды"
        )
        if (profileMember.team.isEmpty()) throw IllegalArgumentException(
            "Введите кем приходится вам участник"
        )
        return profileMember
    }

    private fun validateRussianPhoneNumber(phoneNumber: String): Boolean {
        val regexPattern = "^\\+7 \\(\\d{3}\\) \\d{3}-\\d{2}-\\d{2}\$"
        return !Regex(regexPattern).matches(phoneNumber)
    }

    private fun validateEmail(email: String): Boolean {
        val regexPattern = """^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$"""
        return !Regex(regexPattern).matches(email)
    }
}