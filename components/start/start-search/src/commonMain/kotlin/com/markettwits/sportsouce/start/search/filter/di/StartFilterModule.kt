package com.markettwits.sportsouce.start.search.filter.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cahce.Cache
import com.markettwits.cahce.InMemoryCache
import com.markettwits.cahce.execute.base.ExecuteWithCache
import com.markettwits.cahce.execute.base.ExecuteWithCacheBase
import com.markettwits.core.time.BaseTimeMapper
import com.markettwits.core.time.TimeMapper
import com.markettwits.sportsouce.start.cloud.di.sportSauceStartNetworkModule
import com.markettwits.sportsouce.start.search.filter.data.StartFilterRepositoryBase
import com.markettwits.sportsouce.start.search.filter.data.cache.FilterCache
import com.markettwits.sportsouce.start.search.filter.data.mapper.StartFilterDomainToRemoteMapper
import com.markettwits.sportsouce.start.search.filter.data.mapper.StartFilterRemoteToDomainMapper
import com.markettwits.sportsouce.start.search.filter.domain.StartFilter
import com.markettwits.sportsouce.start.search.filter.domain.StartFilterRepository
import com.markettwits.sportsouce.start.search.filter.presentation.store.StartFilerStoreFactory
import com.markettwits.sportsouce.starts.common.di.startsCommonModule
import com.markettwits.sportsouce.teams_city.di.teamsCityModule
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