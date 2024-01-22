package com.markettwits.start.data.registration.mapper

import com.markettwits.cloud.ext_model.DistanceInfo
import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.start_registration.StartRegisterRequest
import com.markettwits.start.domain.StartStatement
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

class RegistrationMapperBase : RegistrationMapper {
    override fun map(
        withoutPayment: Boolean,
        user: User,
        startStatement: StartStatement,
        startDistanceInfo: DistanceInfo,
        startId: Int,
    ): StartRegisterRequest {
        return StartRegisterRequest(
            alone = false,
            day = 1,
            distance = startDistanceInfo.value,
            format = startDistanceInfo.format,
            member = listOf(mapStartMember(user, startStatement, startDistanceInfo)),
            payment_disabled = false,
            payment_type = "",
            price = startDistanceInfo.distance.price.toInt(),
            promocode = startStatement.promocode,
            registration_without_payment = withoutPayment,
            start_id = startId
        )
    }

    private fun mapStartMember(
        user: User,
        startStatement: StartStatement,
        startDistanceInfo: DistanceInfo
    ): StartRegisterRequest.Member {
        return StartRegisterRequest.Member(
            age = calculateAge(startStatement.birthday),
            birthday = convertDateFormat(startStatement.birthday),
            cite = null,
            city = startStatement.city,
            contactPerson = true,
            email = startStatement.email,
            fromContacts = true,
            gender = mapGender(startStatement.sex),
            group = mapGroup(startStatement, startDistanceInfo),
            instagram = "",
            name = startStatement.name,
            phone = startStatement.phone,
            price = startStatement.price.toInt(),
            promo = startStatement.promocode,
            stage = "",
            surname = startStatement.surname,
            team = startStatement.team,
            teamNumber = 1,
            telegram = user.telegram,
            type = "",
            user_id = user.id,
            vk = user.vk,
            whatsapp = user.whatsapp
        )
    }

    private fun mapGroup(
        startStatement: StartStatement,
        startDistanceInfo: DistanceInfo
    ): StartRegisterRequest.Group {
        val age = calculateAge(startStatement.birthday)

        val listOfGroups = startDistanceInfo.groups

            for (group in listOfGroups) {
                val ageFrom = group.ageFrom.toIntOrNull()
                val ageTo = group.ageTo.toIntOrNull()
                if (ageFrom != null && ageTo != null && age in ageFrom..ageTo) {
                    return StartRegisterRequest.Group(
                        ageFrom = group.ageFrom,
                        ageTo = group.ageTo,
                        name = group.name,
                        sex = mapGender(startStatement.sex)
                    )
                }
        }
        // Handle the case when no matching group is found
        // You can throw an exception or return a default group, depending on your requirements.
        throw NoSuchElementException("Нет подходящей возрастной группы для участников $age лет")
    }

    private fun calculateAge(birthday: String): Int {
        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val birthLocalDate = LocalDate.parse(birthday, dateFormatter)
        val currentDate = LocalDate.now()
        val yearsPassed = ChronoUnit.YEARS.between(birthLocalDate, currentDate).toInt()
        return yearsPassed
    }

    private fun convertDateFormat(inputDate: String): String {
        val inputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val pareseDate = inputFormat.parse(inputDate)
        val zoneId = ZoneId.of("Z")
        val zonedDateTime = ZonedDateTime.ofInstant(pareseDate.toInstant(), zoneId)
        val outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
        return outputFormat.format(zonedDateTime)
    }

    private fun mapGender(sex: String): String {
        return when (sex) {
            "Мужской" -> "male"
            "Женский" -> "female"
            else -> "male"
        }
    }
}