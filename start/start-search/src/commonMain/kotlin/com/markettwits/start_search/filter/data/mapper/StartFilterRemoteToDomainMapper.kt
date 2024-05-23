package com.markettwits.start_search.filter.data.mapper

import com.markettwits.cloud.model.city.CityRemote
import com.markettwits.cloud.model.kind_of_sport.KindOfSportRemote
import com.markettwits.cloud.model.seasons.StartSeasonsRemote
import com.markettwits.start_search.filter.domain.StartFilter

internal interface StartFilterRemoteToDomainMapper {
    fun map(
        kindOfSportRemote: KindOfSportRemote,
        seasonsRemote: StartSeasonsRemote,
        cities: CityRemote,
    ): StartFilter


    class Base : StartFilterRemoteToDomainMapper {
        override fun map(
            kindOfSportRemote: KindOfSportRemote,
            seasonsRemote: StartSeasonsRemote,
            cities: CityRemote,
        ): StartFilter = StartFilter(
            kindOfSport = kindOfSportRemote.rows.map { it.name },
            startSeason = seasonsRemote.rows.map { it.name },
            startStatus = mapStartStatus(),
            city = cities.rows.map { it.name },
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