package com.markettwits.start_search.filter.data.mapper

import com.markettwits.start_cloud.model.kindofsport.KindOfSportRemote
import com.markettwits.start_cloud.model.seasons.StartSeasonsRemote
import com.markettwits.start_search.filter.domain.StartFilter
import com.markettwits.teams_city.domain.City

internal interface StartFilterRemoteToDomainMapper {
    fun map(
        kindOfSportRemote: KindOfSportRemote,
        seasonsRemote: StartSeasonsRemote,
        cities: List<City>,
    ): StartFilter


    class Base : StartFilterRemoteToDomainMapper {
        override fun map(
            kindOfSportRemote: KindOfSportRemote,
            seasonsRemote: StartSeasonsRemote,
            cities: List<City>,
        ): StartFilter = StartFilter(
            kindOfSport = kindOfSportRemote.rows.map { it.name },
            startSeason = seasonsRemote.rows.map { it.name },
            startStatus = mapStartStatus(),
            city = cities.map { it.name },
            sorted = StartFilter.Sorted.FirstBefore
        )

        private fun mapStartStatus(): List<StartFilter.StartActual> {
            return listOf(
                StartFilter.StartActual(
                    "Актуальные",
                    listOf(1, 2, 3, 4, 5)
                ),
                StartFilter.StartActual(
                    "Прошедшие",
                    listOf(0, 6)
                )
            )
        }
    }
}