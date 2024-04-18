package com.markettwits.members.member_add_edit.domain.validate


import com.markettwits.members.member_common.domain.ProfileMember

abstract class AddOrEditMemberValidatorAbstract : AddOrEditMemberValidator {

    protected fun validateProfileMember(profileMember: ProfileMember): ProfileMember {
        if (profileMember.name.isEmpty()) throw IllegalArgumentException(
            "Имя не должно быть пустым"
        )
        if (profileMember.surname.isEmpty()) throw IllegalArgumentException(
            "Фамилия не должно быть пустой"
        )
        if (profileMember.phone.isEmpty()) throw IllegalArgumentException(
            "Введите корректый номер телефона"
        )
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        if (profileMember.email.matches(emailRegex)
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
}