package com.markettwits.teams_city.di

import Sportsouce.teams_city.BuildConfig
import com.markettwits.cahce.execute.base.ExecuteWithCache
import com.markettwits.cahce.execute.base.ExecuteWithCacheBase
import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.core_cloud.provider.HttpClientProviderBase
import com.markettwits.core_cloud.provider.JsonProviderBase
import com.markettwits.teams_city.data.TeamsCityRepository
import com.markettwits.teams_city.data.TeamsCityRepositoryBase
import com.markettwits.teams_city.data.cache.TeamsAndCitiesCache
import com.markettwits.teams_city.data.network.SportSauceNetworkTeamsCityApi
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
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
                baseUrl = BuildConfig.SPORTSAUCE_API_PATH
            )
        )
    }
}