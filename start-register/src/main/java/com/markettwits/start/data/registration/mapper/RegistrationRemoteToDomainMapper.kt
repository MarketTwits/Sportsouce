package com.markettwits.start.data.registration.mapper

import android.util.Log
import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.city.CityRemote
import com.markettwits.cloud.model.team.TeamsRemote
import com.markettwits.core_ui.time.TimeMapper
import com.markettwits.core_ui.time.TimePattern
import com.markettwits.start.domain.StartStatement
import com.markettwits.start.presentation.order.domain.OrderDistance
import com.markettwits.start.presentation.order.domain.OrderPrice
import com.markettwits.start.presentation.order.domain.OrderStatement

interface RegistrationRemoteToDomainMapper {
    @Deprecated("change to order")
    fun map(
        cities: CityRemote,
        teamsRemote: TeamsRemote,
        user: User,
        distanceItem: DistanceItem,
        paymentDisabled: Boolean
    ): StartStatement

    fun mapOrder(
        cities: CityRemote,
        teamsRemote: TeamsRemote,
        user: User,
        distanceItem: DistanceItem,
        paymentDisabled: Boolean
    ): OrderStatement
}

class RegistrationRemoteToDomainMapperBase(private val timeMapper: TimeMapper) :
    RegistrationRemoteToDomainMapper {
    override fun map(
        cities: CityRemote,
        teamsRemote: TeamsRemote,
        user: User,
        distanceItem: DistanceItem,
        paymentDisabled: Boolean
    ): StartStatement {
        val price = when (distanceItem) {
            is DistanceItem.DistanceCombo -> distanceItem.price.toString()
            is DistanceItem.DistanceInfo -> distanceItem.distance.price
        }
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
            price = price,
            paymentDisabled = paymentDisabled
        )
    }

    override fun mapOrder(
        cities: CityRemote,
        teamsRemote: TeamsRemote,
        user: User,
        distanceItem: DistanceItem,
        paymentDisabled: Boolean
    ): OrderStatement {
        return OrderStatement(
            distanceInfo = mapOrderDistance(distanceItem),
            members = mapMembers(cities, teamsRemote, user, distanceItem, paymentDisabled),
            promo = "",
            withPayment = paymentDisabled,
            orderPrice = mapOrderPrice(distanceItem, paymentDisabled)
        )
    }

    private fun mapOrderPrice(
        distanceItem: DistanceItem,
        paymentDisabled: Boolean
    ): OrderPrice {
        val price = when (distanceItem) {
            is DistanceItem.DistanceCombo -> distanceItem.price.toString()
            is DistanceItem.DistanceInfo -> distanceItem.distance.price
        }
        return OrderPrice(
            total = price,
            membersCount = 0,
            discountInCache = 0,
            discountInPercent = 0
        )
    }

    private fun mapOrderDistance(distanceItem: DistanceItem): OrderDistance {
        val distances = when (distanceItem) {
            is DistanceItem.DistanceCombo -> distanceItem.distances.map { it.value }
            is DistanceItem.DistanceInfo -> listOf(distanceItem.value)
        }
        val format = when (distanceItem) {
            is DistanceItem.DistanceCombo -> distanceItem.value
            is DistanceItem.DistanceInfo -> distanceItem.format
        }
        return OrderDistance(
            format = format,
            distances = distances
        )
    }

    private fun mapMembers(
        cities: CityRemote,
        teamsRemote: TeamsRemote,
        user: User,
        distanceItem: DistanceItem,
        paymentDisabled: Boolean
    ): List<StartStatement> {
        return when (distanceItem) {
            is DistanceItem.DistanceCombo -> singleMember(
                cities,
                teamsRemote,
                user,
                distanceItem,
                paymentDisabled
            )

            is DistanceItem.DistanceInfo -> {
                val stages = distanceItem.groups[0].stages
                Log.e("mt05", "#mapMembers $stages")
                if (stages.isNullOrEmpty()) {
                    singleMember(cities, teamsRemote, user, distanceItem, paymentDisabled)
                } else {
                    mapGroups(cities, teamsRemote, user, distanceItem, paymentDisabled)
                }
            }
        }
    }

    private fun singleMember(
        cities: CityRemote,
        teamsRemote: TeamsRemote,
        user: User,
        distanceItem: DistanceItem,
        paymentDisabled: Boolean
    ): List<StartStatement> =
        listOf(map(cities, teamsRemote, user, distanceItem, paymentDisabled))

    private fun mapGroups(
        cities: CityRemote,
        teamsRemote: TeamsRemote,
        user: User,
        distanceItem: DistanceItem.DistanceInfo,
        paymentDisabled: Boolean
    ): List<StartStatement> {
        val startStatements = mutableListOf<StartStatement>()
        startStatements.add(map(cities, teamsRemote, user, distanceItem, paymentDisabled))
        val stageCount = distanceItem.groups[0].stages?.size ?: 1
        for (i in 1 until stageCount) {
            startStatements.add(
                StartStatement(
                    name = "",
                    surname = "",
                    birthday = "",
                    age = 0,
                    email = "",
                    sex = "",
                    city = "",
                    team = "",
                    phone = "",
                    promocode = "",
                    price = "",
                    contactPerson = false,
                    cities = mapCitiesToDomain(cities.rows),
                    teams = mapTeamsToDomain(teamsRemote.rows),
                    sexList = mapSexToDomain(),
                    paymentDisabled = paymentDisabled
                )
            )
        }
        return startStatements
    }
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
