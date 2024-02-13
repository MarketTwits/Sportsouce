package com.markettwits.start_filter.start_filter.data.mapper

import com.markettwits.cloud.model.city.CityRemote
import com.markettwits.cloud.model.kind_of_sport.KindOfSportRemote
import com.markettwits.cloud.model.seasons.StartSeasonsRemote
import com.markettwits.start_filter.start_filter.domain.StartFilter

internal interface StartFilterRemoteToDomainMapper {
    fun map(
        kindOfSportRemote: KindOfSportRemote,
        seasonsRemote: StartSeasonsRemote,
        cities: CityRemote
    ): StartFilter

    class Base : StartFilterRemoteToDomainMapper {
        override fun map(
            kindOfSportRemote: KindOfSportRemote,
            seasonsRemote: StartSeasonsRemote,
            cities: CityRemote
        ): StartFilter {
            return StartFilter(
                kindOfSport = kindOfSportRemote.rows.map { it.name },
                startSeason = seasonsRemote.rows.map { it.name },
                startStatus = mapStartStatus(),
                city = cities.rows.map { it.name },
                fromDate = "Хз какая "
            )
        }
        private fun mapStartStatus(): List<StartFilter.StartActual> {
            return listOf(
                StartFilter.StartActual(
                    "Актуальные",
                    listOf(1, 2, 3, 4, 5)
                ),
                StartFilter.StartActual(
                    "Прошедшие",
                    listOf(0,6)
                )
            )
        }
    }
}
interface ABCD{
    class Aplpha : ABCD
}

