package com.markettwits.sportsouce.start.register.presentation.registration.common.data.mapper

import com.markettwits.core.time.TimeMapper
import com.markettwits.core.time.TimePattern
import com.markettwits.sportsouce.auth.cloud.model.sign_in.response.User
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import com.markettwits.sportsouce.profile.members.member_common.domain.emptyProfileMember
import com.markettwits.sportsouce.start.cloud.model.register.price.fields.StartRegisterAnswer
import com.markettwits.sportsouce.start.cloud.model.start.fields.AdditionalField
import com.markettwits.sportsouce.start.cloud.model.start.fields.DistinctDistance
import com.markettwits.sportsouce.start.cloud.model.start.fields.Stage
import com.markettwits.sportsouce.start.register.domain.StartStatement
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationAdditionalField
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationDistance
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationFieldPrice
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationStage
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationStageWithStatement
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationStatementAnswer
import com.markettwits.sportsouce.teams_city.domain.City
import com.markettwits.sportsouce.teams_city.domain.Team
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

class StartRegistrationPageMapper(
    private val timeMapper: TimeMapper
) {

    fun mapToStartRegistrationDistance(
        user: User,
        distinctDistances: List<DistinctDistance>,
        cities: List<City>,
        teams: List<Team>,
        members: List<ProfileMember>,
    ): List<StartRegistrationDistance> {
        return distinctDistances.map {
            StartRegistrationDistance(
                id = it.id,
                description = it.description ?: "",
                title = it.name,
                //additionalFields = mapStartAdditionalFields(it.additional_fields ?: emptyList()),
                stages = mapStarStages(
                    user = user,
                    stages = it.stages,
                    members = members,
                    cities = cities,
                    teams = teams
                ),
                price = mapStartRegistrationPrice(it.staticPrice),
                format = it.format ?: "",
                answers = mapStartAdditionalFields(it.additionalFields ?: emptyList()).map {
                    StartRegistrationStatementAnswer(
                        field = it,
                        answer = StartRegisterAnswer(
                            additionalFieldId = it.id,
                            isOptional = it.isOptional,
                            type = it.type.value
                        )
                    )
                }
            )
        }
    }

    private fun mapStartStatement(
        stageId: Int,
        user: User,
        profileMembers: List<ProfileMember>,
        cities: List<City>,
        teams: List<Team>
    ): StartStatement {
        val birthday = calculateBirthday(user.birthday)
        return StartStatement(
            name = user.name,
            surname = user.surname,
            birthday = birthday,
            age = calculateAge(birthday).toString(),
            email = user.email ?: "",
            sex = user.sex,
            city = user.address ?: "",
            team = user.team ?: "",
            phone = user.number,
            promocode = "",
            cities = mapCitiesToStartStatement(cities),
            teams = mapTeamsToStartStatement(teams),
            sexList = mapSexToDomain(),
            price = 0,
            paymentDisabled = false,
            yearDiscountApplied = false,
            distanceTitle = "",
            userId = StartStatement.UserId.WithId(userId = user.id.toString()),
            stageId = stageId,
            members = profileMembers,
        )
    }

    private fun mapStartStatement(
        stageId: Int,
        profileMember: ProfileMember,
        profileMembers: List<ProfileMember>,
        cities: List<City>,
        teams: List<Team>
    ): StartStatement {
        val birthday = calculateBirthday(profileMember.birthday)
        return StartStatement(
            name = profileMember.name,
            surname = profileMember.surname,
            birthday = birthday,
            age = calculateAge(birthday).toString(),
            email = profileMember.email,
            sex = profileMember.gender,
            city = "",
            team = profileMember.team,
            phone = profileMember.phone,
            promocode = "",
            cities = mapCitiesToStartStatement(cities),
            teams = mapTeamsToStartStatement(teams),
            sexList = mapSexToDomain(),
            price = 0,
            paymentDisabled = false,
            yearDiscountApplied = false,
            distanceTitle = "",
            userId = if (profileMember.userId == 0)
                StartStatement.UserId.WithoutId
            else
                StartStatement.UserId.WithId(userId = profileMember.userId.toString()),
            stageId = stageId,
            members = profileMembers,
            // answers = emptyList()
        )
    }

    private fun calculateBirthday(birthdayRemote: String): String {
        return try {
            timeMapper.mapTime(TimePattern.FullWithDots, birthdayRemote)
        } catch (e: Exception) {
            ""
        }
    }

    private fun calculateAge(birthdaySimple: String): Int {
        return try {
            val parts = birthdaySimple.split(".") // Разбиваем строку "dd.MM.yyyy"
            val birthLocalDate = LocalDate(parts[2].toInt(), parts[1].toInt(), parts[0].toInt()) // LocalDate(year, month, day)

            val currentDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

            val years = currentDate.year - birthLocalDate.year - if (
                currentDate.monthNumber < birthLocalDate.monthNumber ||
                (currentDate.monthNumber == birthLocalDate.monthNumber && currentDate.dayOfMonth < birthLocalDate.dayOfMonth)
            ) 1 else 0

            years
        } catch (e: Exception) {
            0
        }
    }

    private fun mapCitiesToStartStatement(city: List<City>): List<StartStatement.City> =
        city.map {
            StartStatement.City(id = it.id, name = it.name)
        }

    private fun mapTeamsToStartStatement(city: List<Team>): List<StartStatement.Team> =
        city.map {
            StartStatement.Team(id = it.id, name = it.name)
        }

    private fun mapSexToDomain(): List<StartStatement.Sex> =
        listOf(
            StartStatement.Sex(0, "Мужской"),
            StartStatement.Sex(1, "Женский"),
        )


    private fun mapStarStages(
        user: User,
        stages: List<Stage>,
        members: List<ProfileMember>,
        cities: List<City>,
        teams: List<Team>
    ): List<StartRegistrationStageWithStatement> {
        return stages.mapIndexed { index, stage ->
            StartRegistrationStageWithStatement(
                statement = if (index != 0)
                    mapStartStatement(
                        profileMember = emptyProfileMember,
                        cities = cities,
                        teams = teams,
                        stageId = stage.id,
                        profileMembers = members,
                    )
                else
                    mapStartStatement(
                        user = user,
                        cities = cities,
                        teams = teams,
                        stageId = stage.id,
                        profileMembers = members,
                    ),
                stage = StartRegistrationStage(
                    id = stage.id,
                    distanceId = stage.distanceId,
                    name = stage.name,
                    length = stage.stageLength ?: "",
                    sex = stage.sex ?: "",
                    additionalFields = mapStartAdditionalFields(
                        stage.additionalFields ?: emptyList()
                    ).map {
                        StartRegistrationStatementAnswer(
                            field = it,
                            answer = StartRegisterAnswer(
                                additionalFieldId = it.id,
                                isOptional = it.isOptional,
                                type = it.type.value
                            )
                        )
                    }
                )
            )
        }
    }

    private fun mapStartRegistrationAdditionalFieldsType(type: String): StartRegistrationAdditionalField.Type {
        return when (type) {
            "Текст" -> StartRegistrationAdditionalField.Type.TEXT
            "Число" -> StartRegistrationAdditionalField.Type.NUMBER
            "Чекбокс" -> StartRegistrationAdditionalField.Type.CHECKBOX
            "Дата" -> StartRegistrationAdditionalField.Type.DATA
            "Мультиселект" -> StartRegistrationAdditionalField.Type.MULTISELECT
            "Синглселект" -> StartRegistrationAdditionalField.Type.SINGLE_SELECT
            "Время" -> StartRegistrationAdditionalField.Type.TIME
            else -> throw IllegalArgumentException("unknown $type in AdditionalFieldsType")
        }
    }

    private fun mapStartAdditionalFields(additionalField: List<AdditionalField>): List<StartRegistrationAdditionalField> {
        return additionalField.map { field ->
            StartRegistrationAdditionalField(
                id = field.id,
                type = mapStartRegistrationAdditionalFieldsType(field.type),
                options = field.options,
                price = mapStartRegistrationPrice(field.price),
                title = field.name,
                isOptional = field.is_optional,
                dependentFields = mapStartAdditionalFields(
                    field.dependent_fields ?: emptyList()
                )
            )
        }
    }

    private fun mapStartRegistrationPrice(price: Int?): StartRegistrationFieldPrice =
        if (price == null || price < 1)
            StartRegistrationFieldPrice.Empty
        else StartRegistrationFieldPrice.Cost(price)
}

