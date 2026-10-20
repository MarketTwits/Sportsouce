package com.markettwits.sportsouce.profile.members.member_add_edit.domain.validate

import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import com.markettwits.sportsouce.profile.members.member_common.domain.memberBirthDate
import com.markettwits.sportsouce.profile.members.member_common.domain.normalizeMemberPhone
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

abstract class AddOrEditMemberValidatorAbstract : AddOrEditMemberValidator {
    @OptIn(ExperimentalTime::class)
    protected fun validateProfileMember(profileMember: ProfileMember): ProfileMember {
        val member = profileMember.copy(name = profileMember.name.trim(), surname = profileMember.surname.trim(),
            email = profileMember.email.trim(), phone = profileMember.phone.trim(), team = profileMember.team.trim(),
            gender = profileMember.gender.trim(), type = profileMember.type.trim())
        require(member.name.isNotBlank()) { "Введите имя участника" }
        require(member.surname.isNotBlank()) { "Введите фамилию участника" }
        if (!member.child && member.phone.isNotBlank()) require(Regex("^7[0-9]{10}$").matches(normalizeMemberPhone(member.phone))) {
            "Введите корректный номер телефона"
        }
        if (!member.child && member.email.isNotBlank()) require(Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$").matches(member.email)) {
            "Введите корректную почту"
        }
        for (name in listOf(member.name, member.surname)) {
            require(name.length in 2..20) { "Имя и фамилия должны содержать от 2 до 20 символов" }
            require(Regex("^[а-яА-ЯЁё\\-\\s]+$").matches(name)) { "В имени и фамилии используйте русские буквы, пробелы и дефис" }
        }
        val birthday = runCatching { memberBirthDate(member.birthday) }.getOrNull()
        require(birthday != null) { "Введите корректную дату рождения" }
        require(birthday <= Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date) {
            "Дата рождения не может быть в будущем"
        }
        require(member.gender.isNotBlank()) { "Выберите пол участника" }
        require(member.type.isNotBlank()) { "Укажите, кем вам приходится участник" }
        require(member.team.isNotBlank()) { "Выберите команду или участие лично" }
        // Contacts are optional; children follow the web form without contact fields.
        return member
    }
}
