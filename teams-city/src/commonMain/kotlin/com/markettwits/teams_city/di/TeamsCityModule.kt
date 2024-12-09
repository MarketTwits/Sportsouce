package com.markettwits.teams_city.di

import com.markettwits.cahce.execute.base.ExecuteWithCache
import com.markettwits.cahce.execute.base.ExecuteWithCacheBase
import com.markettwits.cahce.execute.list.ExecuteListWithCache
import com.markettwits.cahce.execute.list.ExecuteListWithCacheBase
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.teams_city.data.TeamsCityRepository
import com.markettwits.teams_city.data.TeamsCityRepositoryBase
import com.markettwits.teams_city.data.cache.TeamsAndCitiesCache
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val teamsCityModule = module {
    includes(sportSouceNetworkModule)
    singleOf(::TeamsAndCitiesCache)
    singleOf(::ExecuteWithCacheBase) bind ExecuteWithCache::class
    singleOf(::TeamsCityRepositoryBase) bind TeamsCityRepository::class
}