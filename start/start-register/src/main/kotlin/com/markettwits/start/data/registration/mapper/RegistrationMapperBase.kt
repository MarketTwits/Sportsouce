package com.markettwits.start.data.registration.mapper

import android.util.Log
import android.util.Patterns
import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.start_registration.StartRegisterRequest
import com.markettwits.start.domain.StartStatement
import com.markettwits.start.presentation.order.domain.OrderStatement
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale


class RegistrationMapperBase : RegistrationMapper {
    override fun mapBase(
        withoutPayment: Boolean,
        user: User,
        startStatement: StartStatement,
        startDistanceItem: DistanceItem.DistanceInfo,
        startId: Int,
    ): StartRegisterRequest {
        return StartRegisterRequest(
            alone = false,
            day = 1,
            distance = startDistanceItem.value,
            format = startDistanceItem.format,
            member = listOf(
                mapStartMember(
                    user = user,
                    teamNumber = 1,
                    contactPerson = true,
                    startStatement = validateFields(startStatement),
                    startDistanceInfo = startDistanceItem
                )
            ),
            payment_disabled = false,
            payment_type = "",
            price = startDistanceItem.distance.price.toInt(),
            promocode = startStatement.promocode,
            registration_without_payment = withoutPayment,
            start_id = startId
        )
    }

    override fun mapCombo(
        withoutPayment: Boolean,
        user: User,
        startStatement: StartStatement,
        startDistanceItem: DistanceItem.DistanceCombo,
        startId: Int
    ): StartRegisterRequest.Combo {
        return StartRegisterRequest.Combo(
            alone = false,
            day = 1,
            distance = startDistanceItem.value,
            format = "Лично",
            distances = mapStartComboDistances(
                user = user,
                1,
                contactPerson = true,
                startStatement = validateFields(startStatement),
                items = startDistanceItem.distances
            ),
            payment_disabled = false,
            payment_type = "",
            price = startDistanceItem.price ?: 0,
            promocode = startStatement.promocode,
            registration_without_payment = withoutPayment,
            start_id = startId
        )
    }

    override fun mapNewBase(
        withoutPayment: Boolean,
        user: User,
        startStatement: OrderStatement,
        startDistanceItem: DistanceItem.DistanceInfo,
        startId: Int
    ): StartRegisterRequest {
        return StartRegisterRequest(
            alone = false,
            day = 1,
            distance = startDistanceItem.value,
            format = startDistanceItem.format,
            member = startStatement.members.mapIndexed { index, member ->
                mapStartMember(
                    user = user,
                    teamNumber = index + 1,
                    contactPerson = member.contactPerson,
                    startStatement = member,
                    startDistanceInfo = startDistanceItem

                )
            },
            payment_disabled = startStatement.paymentDisabled,
            payment_type = startStatement.paymentType,
            price = startStatement.orderPrice.total.toInt(),
            promocode = startStatement.promo,
            registration_without_payment = withoutPayment,
            start_id = startId
        )
    }

    override fun mapNewCombo(
        withoutPayment: Boolean,
        user: User,
        startStatement: OrderStatement,
        startDistanceItem: DistanceItem.DistanceCombo,
        startId: Int
    ): StartRegisterRequest.Combo {
        return StartRegisterRequest.Combo(
            alone = false,
            day = 1,
            distance = startDistanceItem.value,
            format = "Лично",
            distances = mapNewStartComboDistances(
                user = user,
                startStatement = startStatement.members,
                items = startDistanceItem.distances
            ),
            payment_disabled = startStatement.paymentDisabled,
            payment_type = startStatement.paymentType,
            price = startStatement.orderPrice.total.toInt(),
            promocode = startStatement.promo,
            registration_without_payment = withoutPayment,
            start_id = startId
        )
    }

    private fun mapNewStartComboDistances(
        user: User,
        startStatement: List<StartStatement>,
        items: List<DistanceItem.DistanceInfo>
    ): List<StartRegisterRequest.Combo.Distance> {
        Log.e("mt05", "#mapStartComboDistances " + items.toString())
        val values = items.map { distance ->
            StartRegisterRequest.Combo.Distance(
                alone = false,
                distance = distance.value,
                format = distance.format,
                member = startStatement.mapIndexed { index, member ->
                    mapStartMember(
                        user = user,
                        teamNumber = index + 1,
                        contactPerson = member.contactPerson,
                        startStatement = member,
                        startDistanceInfo = distance
                    )
                }
            )
        }
        return values
    }

    private fun mapStartComboDistances(
        user: User,
        teamNumber: Int,
        contactPerson: Boolean,
        startStatement: StartStatement,
        items: List<DistanceItem.DistanceInfo>
    ): List<StartRegisterRequest.Combo.Distance> {
        Log.e("mt05", "#mapStartComboDistances " + items.toString())
        val values = items.map {
            StartRegisterRequest.Combo.Distance(
                alone = false,
                distance = it.value,
                format = it.format,
                member = listOf(
                    mapStartMember(
                        user = user,
                        teamNumber = teamNumber,
                        contactPerson = contactPerson,
                        startStatement = startStatement,
                        startDistanceInfo = it
                    )
                )
            )
        }
        return values
    }


    private fun validateFields(startStatement: StartStatement): StartStatement {
        if (startStatement.city.isEmpty() || startStatement.city.length < 5) throw IllegalStateException(
            "Введите корректное название города (не менее 5 символов)"
        )
        if (startStatement.team.isEmpty()) throw IllegalArgumentException(
            "Введите корректное название команды (не менее 5 символов)"
        )
        if (startStatement.name.isEmpty()) throw IllegalArgumentException(
            "Имя не должно быть пустым"
        )
        if (startStatement.surname.isEmpty()) throw IllegalArgumentException(
            "Фамилия не должно быть пустой"
        )
        if (!Patterns.EMAIL_ADDRESS.matcher(startStatement.email)
                .matches()
        ) throw IllegalArgumentException(
            "Введите корректую почту"
        )
        if (startStatement.phone.isEmpty()) throw IllegalArgumentException(
            "Введите корректый номер телефона"
        )
        return startStatement
    }

    private fun mapStartMember(
        user: User,
        teamNumber: Int,
        contactPerson: Boolean,
        startStatement: StartStatement,
        startDistanceInfo: DistanceItem.DistanceInfo
    ): StartRegisterRequest.Member {
        return StartRegisterRequest.Member(
            age = calculateAge(startStatement.birthday),
            birthday = convertDateFormat(startStatement.birthday),
            cite = null,
            city = startStatement.city,
            contactPerson = contactPerson,
            email = startStatement.email,
            fromContacts = true,
            gender = mapGender(startStatement.sex),
            group = mapGroup(startStatement, startDistanceInfo),
            instagram = "",
            name = startStatement.name,
            phone = startStatement.phone,
            price = startStatement.price,
            promo = startStatement.promocode,
            stage = mapStage(teamNumber, startStatement.sex),
            surname = startStatement.surname,
            team = startStatement.team,
            teamNumber = teamNumber,
            telegram = user.telegram,
            type = startDistanceInfo.format,
            user_id = user.id,
            vk = user.vk,
            whatsapp = user.whatsapp
        )
    }

    private fun mapStage(teamNumber: Int, sex: String): DistanceItem.Stage {
        return DistanceItem.Stage("$teamNumber этап", listOf(sex))
    }

    private fun mapGroup(
        startStatement: StartStatement,
        startDistanceInfo: DistanceItem.DistanceInfo
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
                    stages = group.stages ?: emptyList(),
                    sex = mapGender(startStatement.sex)
                )
            }
        }
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
            "Мужской" -> "Мужской"
            "Женский" -> "Женский"
            else -> "male"
        }
    }
}