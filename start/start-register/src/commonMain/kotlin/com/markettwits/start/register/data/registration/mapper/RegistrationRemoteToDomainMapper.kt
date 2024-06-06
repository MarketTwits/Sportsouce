package com.markettwits.start.register.data.registration.mapper

import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.members.member_common.domain.ProfileMember
import com.markettwits.start.register.domain.StartStatement
import com.markettwits.start.register.presentation.order.domain.OrderDistance
import com.markettwits.start.register.presentation.order.domain.OrderPrice
import com.markettwits.start.register.presentation.order.domain.OrderStatement
import com.markettwits.teams_city.domain.City
import com.markettwits.teams_city.domain.Team
import com.markettwits.time.TimeMapper
import com.markettwits.time.TimePattern
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

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
        paymentType: String,
        discounts: List<DistanceItem.Discount>
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
            is DistanceItem.DistanceCombo -> calculatePriceForComboStartStartStatement(distanceItem)
            is DistanceItem.DistanceInfo -> calculatePriceForStartStartStatement(distanceItem)
        }
        val birthday = timeMapper.mapTime(TimePattern.FullWithDots, user.birthday)
        return StartStatement(
            name = user.name,
            surname = user.surname,
            birthday = birthday,
            age = user.age ?: calculateAge(birthday).toString(),
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
            paymentDisabled = paymentDisabled,
            yearDiscountApplied = false,
            distanceTitle = when (distanceItem) {
                is DistanceItem.DistanceCombo -> distanceItem.value
                is DistanceItem.DistanceInfo -> distanceItem.value
            }

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
        paymentType: String,
        discounts: List<DistanceItem.Discount>
    ): OrderStatement {
        val distanceInfo = mapDistanceItem(distanceItem, cities, teams, user, paymentDisabled)
        return OrderStatement(
            distanceInfo = distanceInfo,
            //mapOrderDistance(distanceItem, cities, teams, user, paymentDisabled),
            promo = "",
            paymentDisabled = paymentDisabled,
            paymentType = paymentType,
            profileMembers = members,
            orderTitle = startTitle,
            orderPrice = mapOrderPrice(distanceItem, distanceInfo.map { it.members }.size),
            discounts = discounts,
            currentOrderDistanceVisibleIndex = 0
        )
    }

    private fun mapDistanceItem(
        distanceItem: DistanceItem,
        cities: List<City>,
        teams: List<Team>,
        user: User,
        paymentDisabled: Boolean
    ): List<OrderDistance> {
        return when (distanceItem) {
            is DistanceItem.DistanceCombo -> {
                distanceItem.distances.map { distance ->
                    OrderDistance(
                        format = distance.format,
                        distance = distance.value,
                        members = mapBaseMembers(cities, teams, user, distance, paymentDisabled)
                    )
                }
            }

            is DistanceItem.DistanceInfo -> {
                listOf(
                    OrderDistance(
                        format = distanceItem.format,
                        distance = distanceItem.value,
                        members = mapBaseMembers(cities, teams, user, distanceItem, paymentDisabled)
                    )
                )
            }
        }
    }

    private fun mapOrderPrice(
        distanceItem: DistanceItem,
        membersCount: Int
    ): OrderPrice {
        val price = when (distanceItem) {
            is DistanceItem.DistanceCombo -> distanceItem.price ?: 0
            is DistanceItem.DistanceInfo -> distanceItem.distance.price.toInt()
        }
        val discount = when (distanceItem) {
            is DistanceItem.DistanceCombo -> distanceItem.sale?.toInt() ?: 0
            is DistanceItem.DistanceInfo -> 0
        }
        return OrderPrice(
            total = price,
            initialTotal = price,
            //totalAfterCombo = price - calculateDiscount(price,discount),
            membersCount = membersCount,
            discountPromoInCache = 0,
            discountAgeInCache = 0,
            discountComboInCache = if (distanceItem is DistanceItem.DistanceCombo) calculateDiscount(
                price,
                discount
            ) else 0,
            promoDiscountInPercent = 0

        )
    }

    private fun calculateDiscount(originalCost: Int, discountPercentage: Int): Int =
        if (discountPercentage != 0) (originalCost / discountPercentage) else originalCost

    private fun mapBaseMembers(
        cities: List<City>,
        teams: List<Team>,
        user: User,
        distanceItem: DistanceItem.DistanceInfo,
        paymentDisabled: Boolean
    ): List<StartStatement> {
        val stages = distanceItem.groups[0].stages ?: distanceItem.distanceStages
        return if (stages.isNullOrEmpty()) {
            singleMember(cities, teams, user, distanceItem, paymentDisabled)
        } else {
            mapGroups(cities, teams, user, distanceItem, paymentDisabled)
        }
    }

    private fun singleMember(
        cities: List<City>,
        teams: List<Team>,
        user: User,
        distanceItem: DistanceItem,
        paymentDisabled: Boolean
    ): List<StartStatement> = listOf(map(cities, teams, user, distanceItem, paymentDisabled))

    private fun mapGroups(
        cities: List<City>,
        teams: List<Team>,
        user: User,
        distanceItem: DistanceItem.DistanceInfo,
        paymentDisabled: Boolean
    ): List<StartStatement> {
        val startStatements = mutableListOf<StartStatement>()
        startStatements.add(map(cities, teams, user, distanceItem, paymentDisabled))
        val stageCount =
            distanceItem.groups[0].stages?.size ?: distanceItem.distanceStages?.size ?: 1
        for (i in 1 until stageCount) {
            startStatements.add(
                StartStatement(
                    name = "",
                    surname = "",
                    birthday = "",
                    age = "",
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
                    paymentDisabled = paymentDisabled,
                    yearDiscountApplied = false,
                    distanceTitle = distanceItem.value
                )
            )
        }
        return startStatements
    }
}

private fun calculatePriceForStartStartStatement(distanceItem: DistanceItem.DistanceInfo): Int {
    val stageCount = distanceItem.groups[0].stages?.size ?: 1
    val totalPrice = distanceItem.distance.price.toIntOrNull() ?: 0
    val memberPrice = totalPrice / stageCount
    return memberPrice
}

private fun calculatePriceForComboStartStartStatement(distanceItem: DistanceItem.DistanceCombo): Int {
    val stageCount = distanceItem.distances.map { it.distanceStages }.size
    val totalPrice = distanceItem.price ?: 0
    val memberPrice = totalPrice / stageCount
    return memberPrice
}

private fun calculateAge(birthday: String): Int {
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val birthLocalDate = LocalDate.parse(birthday, dateFormatter)
    val currentDate = LocalDate.now()
    return ChronoUnit.YEARS.between(birthLocalDate, currentDate).toInt()
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
