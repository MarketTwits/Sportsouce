package com.markettwits.start.data.registration.mapper

import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.city.CityRemote
import com.markettwits.cloud.model.team.TeamsRemote
import com.markettwits.core_ui.time.TimeMapper
import com.markettwits.core_ui.time.TimePattern
import com.markettwits.start.domain.StartStatement

interface RegistrationRemoteToDomainMapper {
    fun map(cities: CityRemote, teamsRemote: TeamsRemote, user: User,price : String): StartStatement
}

class RegistrationRemoteToDomainMapperBase(private val timeMapper: TimeMapper) :
    RegistrationRemoteToDomainMapper {
    override fun map(cities: CityRemote, teamsRemote: TeamsRemote, user: User, price : String): StartStatement {
        return StartStatement(
            name = user.name,
            surname = user.surname,
            birthday = timeMapper.mapTime(TimePattern.FullWithDots, user.birthday),
            age = user.age?.toInt() ?: 0,
            email = user.email,
            sex = user.sex,
            city = user.address ?: "",
            team = user.team ?: "",
            phone = user.number,
            promocode = "",
            cities = mapCitiesToDomain(cities.rows),
            teams = mapTeamsToDomain(teamsRemote.rows),
            sexList = mapSexToDomain(),
            price = price
        )
    }

    private fun mapCitiesToDomain(city: List<CityRemote.Row>): List<StartStatement.City> =
        city.map {
            StartStatement.City(id = it.id, name = it.name)
        }

    private fun mapTeamsToDomain(city: List<TeamsRemote.Row>): List<StartStatement.Team> =
        city.map {
            StartStatement.Team(id = it.id, name = it.name)
        }

    private fun mapSexToDomain(): List<StartStatement.Sex> =
        listOf(
            StartStatement.Sex(0, "Мужской"),
            StartStatement.Sex(1, "Женский"),
        )

}