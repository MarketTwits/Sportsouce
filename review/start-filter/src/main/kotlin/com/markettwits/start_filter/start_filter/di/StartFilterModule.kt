package com.markettwits.start_filter.start_filter.di

import com.markettwits.start_filter.start_filter.data.StartFilterDomainToRemoteMapper
import com.markettwits.start_filter.start_filter.data.StartFilterRemoteToDomainMapper
import com.markettwits.start_filter.start_filter.data.StartFilterRepository
import com.markettwits.start_filter.start_filter.data.StartFilterRepositoryBase
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val startFilterModule = module {
    factoryOf(::StartFilterRepositoryBase) bind StartFilterRepository::class
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
            factory { dependencies.startsCloudToUiMapper }
            factory { dependencies.sportsouceApi }
        }
    )
}

