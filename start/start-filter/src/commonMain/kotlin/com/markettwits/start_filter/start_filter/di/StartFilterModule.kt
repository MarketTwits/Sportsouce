package com.markettwits.start_filter.start_filter.di

import com.markettwits.cahce.execute.base.ExecuteWithCache
import com.markettwits.cahce.execute.base.ExecuteWithCacheBase
import com.markettwits.start_filter.start_filter.data.StartFilterRepository
import com.markettwits.start_filter.start_filter.data.StartFilterRepositoryBase
import com.markettwits.start_filter.start_filter.data.cache.FilterCache
import com.markettwits.start_filter.start_filter.data.mapper.StartFilterDomainToRemoteMapper
import com.markettwits.start_filter.start_filter.data.mapper.StartFilterRemoteToDomainMapper
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val startFilterModule = module {
    factoryOf(::StartFilterRepositoryBase) bind StartFilterRepository::class
    factoryOf(::FilterCache)
    factoryOf(::ExecuteWithCacheBase) bind ExecuteWithCache::class
    single<StartFilterRemoteToDomainMapper> {
        StartFilterRemoteToDomainMapper.Base()
    }
    single<StartFilterDomainToRemoteMapper> {
        StartFilterDomainToRemoteMapper.Base()
    }
}

internal fun createStartFilterModules(dependencies: StartsFilterDependencies): List<Module> {
    return listOf(
        startFilterModule,
        module {
            factory { dependencies.sportsouceApi }
            factory { dependencies.startsCloudToListMapper }
        }
    )
}

