package com.markettwits.start.register.data.registration.mapper

import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.start_registration.StartRegisterRequest
import com.markettwits.start.register.domain.StartStatement
import com.markettwits.start.register.presentation.order.domain.OrderStatement
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale


class RegistrationMapperBase : RegistrationMapper {

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
            member =
            mapStartMembersNew(
                user = user,
                startStatement = startStatement,
                startDistanceInfo = startDistanceItem
            ),
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
            gender = mapGenderUiToCloud(startStatement.sex),
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

    private fun mapStartMembersNew(
        user: User,
        startStatement: OrderStatement,
        startDistanceInfo: DistanceItem.DistanceInfo
    ): List<StartRegisterRequest.Member> {
        return startStatement.members.mapIndexed { index, member ->
            StartRegisterRequest.Member(
                age = calculateAge(member.birthday),
                birthday = convertDateFormat(member.birthday),
                cite = null,
                city = member.city,
                contactPerson = member.contactPerson,
                email = member.email,
                fromContacts = true,
                gender = member.sex,
                group = mapGroups(startStatement, startDistanceInfo),
                instagram = "",
                name = member.name,
                phone = member.phone,
                price = member.price,
                promo = member.promocode,
                stage = mapStage(index + 1, member.sex),
                surname = member.surname,
                team = member.team,
                teamNumber = index + 1,
                telegram = user.telegram,
                type = startDistanceInfo.format,
                user_id = user.id,
                vk = user.vk,
                whatsapp = user.whatsapp
            )
        }
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
                    sex = group.sex
                )
            }
        }
        throw NoSuchElementException("Нет подходящей возрастной группы для участников $age лет")
    }

    private fun mapGroups(
        orderStatement: OrderStatement,
        startDistanceInfo: DistanceItem.DistanceInfo
    ): StartRegisterRequest.Group {
        val members = orderStatement.members
        val ageSum = members.sumOf { calculateAge(it.birthday) }
        val listOfGroups = startDistanceInfo.groups

        // Filter groups based on age sum
        val suitableGroups = listOfGroups.filter { group ->
            val ageFrom = group.ageFrom.toIntOrNull()
            val ageTo = group.ageTo.toIntOrNull()
            ageFrom != null && ageTo != null && ageSum in ageFrom..ageTo
        }

        if (suitableGroups.isEmpty()) {
            throw NoSuchElementException("Подходящая возрастная группа не найдена")
        }

        val participantNumbers = members.indices.toList()


        for (group in suitableGroups) {
            val findStages = findStagesForParticipantNumbersAndGender(
                group.stages ?: emptyList(),
                participantNumbers,
                members
            )

            /** Registry command */

            if (findStages != null && findStages == group.stages) {
                return StartRegisterRequest.Group(
                    ageFrom = group.ageFrom,
                    ageTo = group.ageTo,
                    name = group.name,
                    stages = findStages,
                    sex = group.sex
                )
            }
            /** Registry single */

            if (members.size <= 1 && group.sex == mapGenderUiToCloud(members[0].sex)) {
                return StartRegisterRequest.Group(
                    ageFrom = group.ageFrom,
                    ageTo = group.ageTo,
                    name = group.name,
                    sex = group.sex
                )
            }
        }

        throw NoSuchElementException("Не найдено подходящих этапов для участников в возрастных группах")
    }

    private fun findStagesForParticipantNumbersAndGender(
        stagesList: List<DistanceItem.Stage>,
        participantNumbers: List<Int>,
        members: List<StartStatement>,
    ): List<DistanceItem.Stage>? {
        val matchingStages = mutableListOf<DistanceItem.Stage>()
        for (participantNumber in participantNumbers) {
            if (participantNumber >= 0 && participantNumber < stagesList.size) {
                val stage = stagesList[participantNumber]
                if (stage.sex.contains(mapGenderUiToCloud(members[participantNumber].sex))) {
                    matchingStages.add(stage)
                }
            }
        }
        return matchingStages.ifEmpty { null }
    }

    private fun calculateAge(birthday: String): Int {
        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val birthLocalDate = LocalDate.parse(birthday, dateFormatter)
        val currentDate = LocalDate.now()
        return ChronoUnit.YEARS.between(birthLocalDate, currentDate).toInt()
    }

    private fun convertDateFormat(inputDate: String): String {
        val inputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val pareseDate = inputFormat.parse(inputDate)
        val zoneId = ZoneId.of("Z")
        val zonedDateTime = ZonedDateTime.ofInstant(pareseDate.toInstant(), zoneId)
        val outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
        return outputFormat.format(zonedDateTime)
    }


    private fun mapGenderUiToCloud(sex: String): String {
        return when (sex) {
            "Мужской" -> "male"
            "Женский" -> "female"
            else -> throw java.lang.IllegalArgumentException("Нет стартов для $sex пол")
        }
    }
}

