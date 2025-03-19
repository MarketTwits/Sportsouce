package com.markettwits.start_search.filter.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cahce.Cache
import com.markettwits.cahce.InMemoryCache
import com.markettwits.cahce.execute.base.ExecuteWithCache
import com.markettwits.cahce.execute.base.ExecuteWithCacheBase
import com.markettwits.start_cloud.di.sportSauceStartNetworkModule
import com.markettwits.start_search.filter.data.StartFilterRepositoryBase
import com.markettwits.start_search.filter.data.cache.FilterCache
import com.markettwits.start_search.filter.data.mapper.StartFilterDomainToRemoteMapper
import com.markettwits.start_search.filter.data.mapper.StartFilterRemoteToDomainMapper
import com.markettwits.start_search.filter.domain.StartFilter
import com.markettwits.start_search.filter.domain.StartFilterRepository
import com.markettwits.start_search.filter.presentation.store.StartFilerStoreFactory
import com.markettwits.starts_common.di.startsCommonModule
import com.markettwits.teams_city.di.teamsCityModule
import com.markettwits.time.BaseTimeMapper
import com.markettwits.time.TimeMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val startFilterModule = module {
    includes(sportSauceStartNetworkModule, startsCommonModule, teamsCityModule)
    factoryOf(::StartFilterRepositoryBase) bind StartFilterRepository::class
    factoryOf(::FilterCache)
    factoryOf(::ExecuteWithCacheBase) bind ExecuteWithCache::class
    single<StartFilterRemoteToDomainMapper> {
        StartFilterRemoteToDomainMapper.Base()
    }
    single<StartFilterDomainToRemoteMapper> {
        StartFilterDomainToRemoteMapper.Base()
    }
    singleOf(::BaseTimeMapper) bind TimeMapper::class
    single<StartFilerStoreFactory> {
        StartFilerStoreFactory(
            DefaultStoreFactory(),
            get()
        )
    }
    single<Cache<StartFilter>> {
        object : InMemoryCache<StartFilter>() {}
    }
}