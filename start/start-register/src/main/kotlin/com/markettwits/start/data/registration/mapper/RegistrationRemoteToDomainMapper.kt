package com.markettwits.start.data.registration.mapper

import android.util.Log
import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.core_ui.time.TimeMapper
import com.markettwits.core_ui.time.TimePattern
import com.markettwits.members.member_common.domain.ProfileMember
import com.markettwits.start.domain.StartStatement
import com.markettwits.start.presentation.order.domain.OrderDistance
import com.markettwits.start.presentation.order.domain.OrderPrice
import com.markettwits.start.presentation.order.domain.OrderStatement
import com.markettwits.teams_city.domain.City
import com.markettwits.teams_city.domain.Team

interface RegistrationRemoteToDomainMapper {
    @Deprecated("change to order")
    fun map(
        cities: List<City>,
        teams: List<Team>,
        user: User,
        distanceItem: DistanceItem,
        paymentDisabled: Boolean
    ): StartStatement

    fun mapOrder(
        startTitle: String,
        cities: List<City>,
        teams: List<Team>,
        members: List<ProfileMember>,
        user: User,
        distanceItem: DistanceItem,
        paymentDisabled: Boolean,
        paymentType: String
    ): OrderStatement
}

class RegistrationRemoteToDomainMapperBase(private val timeMapper: TimeMapper) :
    RegistrationRemoteToDomainMapper {
    override fun map(
        cities: List<City>,
        teams: List<Team>,
        user: User,
        distanceItem: DistanceItem,
        paymentDisabled: Boolean
    ): StartStatement {
        val price = when (distanceItem) {
            is DistanceItem.DistanceCombo -> distanceItem.price ?: 0
            is DistanceItem.DistanceInfo -> distanceItem.distance.price.toIntOrNull() ?: 0
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
            cities = mapCitiesToStartStatement(cities),
            teams = mapTeamsToStartStatement(teams),
            sexList = mapSexToDomain(),
            price = price,
            paymentDisabled = paymentDisabled
        )
    }

    override fun mapOrder(
        startTitle: String,
        cities: List<City>,
        teams: List<Team>,
        members: List<ProfileMember>,
        user: User,
        distanceItem: DistanceItem,
        paymentDisabled: Boolean,
        paymentType: String
    ): OrderStatement {
        return OrderStatement(
            distanceInfo = mapOrderDistance(distanceItem),
            members = mapMembers(cities, teams, user, distanceItem, paymentDisabled),
            promo = "",
            paymentDisabled = paymentDisabled,
            paymentType = paymentType,
            profileMembers = members,
            orderTitle = startTitle,
            orderPrice = mapOrderPrice(distanceItem)
        )
    }

    private fun mapOrderPrice(
        distanceItem: DistanceItem
    ): OrderPrice {
        val price = when (distanceItem) {
            is DistanceItem.DistanceCombo -> distanceItem.price?.toDouble() ?: 0.0
            is DistanceItem.DistanceInfo -> distanceItem.distance.price.toDouble()
        }
        return OrderPrice(
            total = price,
            membersCount = 0,
            discountInCache = 0.0,
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
        cities: List<City>,
        teams: List<Team>,
        user: User,
        distanceItem: DistanceItem,
        paymentDisabled: Boolean
    ): List<StartStatement> {
        return when (distanceItem) {
            is DistanceItem.DistanceCombo -> singleMember(
                cities,
                teams,
                user,
                distanceItem,
                paymentDisabled
            )

            is DistanceItem.DistanceInfo -> {
                val stages = distanceItem.groups[0].stages
                Log.e("mt05", "#mapMembers $stages")
                if (stages.isNullOrEmpty()) {
                    singleMember(cities, teams, user, distanceItem, paymentDisabled)
                } else {
                    mapGroups(cities, teams, user, distanceItem, paymentDisabled)
                }
            }
        }
    }

    private fun singleMember(
        cities: List<City>,
        teams: List<Team>,
        user: User,
        distanceItem: DistanceItem,
        paymentDisabled: Boolean
    ): List<StartStatement> =
        listOf(map(cities, teams, user, distanceItem, paymentDisabled))

    private fun mapGroups(
        cities: List<City>,
        teams: List<Team>,
        user: User,
        distanceItem: DistanceItem.DistanceInfo,
        paymentDisabled: Boolean
    ): List<StartStatement> {
        val startStatements = mutableListOf<StartStatement>()
        startStatements.add(map(cities, teams, user, distanceItem, paymentDisabled))
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
                    price = 0,
                    contactPerson = false,
                    cities = mapCitiesToStartStatement(cities),
                    teams = mapTeamsToStartStatement(teams),
                    sexList = mapSexToDomain(),
                    paymentDisabled = paymentDisabled
                )
            )
        }
        return startStatements
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
