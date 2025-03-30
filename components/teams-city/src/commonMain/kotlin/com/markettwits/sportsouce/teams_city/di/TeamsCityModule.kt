package com.markettwits.sportsouce.teams_city.di

import com.markettwits.buildkonfig.BuildKonfig
import com.markettwits.cahce.execute.base.ExecuteWithCache
import com.markettwits.cahce.execute.base.ExecuteWithCacheBase
import com.markettwits.core_cloud.provider.HttpClientProviderBase
import com.markettwits.core_cloud.provider.JsonProviderBase
import com.markettwits.sportsouce.teams_city.data.TeamsCityRepositoryBase
import com.markettwits.sportsouce.teams_city.data.cache.TeamsAndCitiesCache
import com.markettwits.sportsouce.teams_city.data.network.SportSauceNetworkTeamsCityApi
import com.markettwits.sportsouce.teams_city.domain.TeamsCityRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val teamsCityModule = module {
    singleOf(::TeamsAndCitiesCache)
    singleOf(::ExecuteWithCacheBase) bind ExecuteWithCache::class
    singleOf(::TeamsCityRepositoryBase) bind TeamsCityRepository::class
    single<SportSauceNetworkTeamsCityApi> {
        SportSauceNetworkTeamsCityApi(
            HttpClientProviderBase(
                json = JsonProviderBase().provide(),
                baseUrl = BuildKonfig.SPORTSAUCE_API_PATH
            )
        )
    }
}