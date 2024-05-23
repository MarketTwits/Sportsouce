package com.markettwits.start_search.filter.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cahce.Cache
import com.markettwits.cahce.InMemoryCache
import com.markettwits.cahce.execute.base.ExecuteWithCache
import com.markettwits.cahce.execute.base.ExecuteWithCacheBase
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.start_search.filter.data.StartFilterRepositoryBase
import com.markettwits.start_search.filter.data.cache.FilterCache
import com.markettwits.start_search.filter.data.mapper.StartFilterDomainToRemoteMapper
import com.markettwits.start_search.filter.data.mapper.StartFilterRemoteToDomainMapper
import com.markettwits.start_search.filter.domain.StartFilter
import com.markettwits.start_search.filter.domain.StartFilterRepository
import com.markettwits.start_search.filter.presentation.store.StartFilerStoreFactory
import com.markettwits.starts_common.data.mapper.StartsCloudToListMapper
import com.markettwits.starts_common.data.mapper.StartsCloudToListMapperBase
import com.markettwits.time.BaseTimeMapper
import com.markettwits.time.TimeMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val startFilterModule = module {
    includes(sportSouceNetworkModule)
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
    singleOf(::StartsCloudToListMapperBase) bind StartsCloudToListMapper::class
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