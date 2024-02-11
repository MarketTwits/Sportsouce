package com.markettwits.start_filter.start_filter.di

import com.markettwits.core_ui.time.BaseTimeMapper
import com.markettwits.start_filter.start_filter.data.StartFilterDomainToRemoteMapper
import com.markettwits.start_filter.start_filter.data.StartFilterRemoteToDomainMapper
import com.markettwits.start_filter.start_filter.data.StartFilterRepository
import com.markettwits.start_filter.start_filter.data.StartFilterRepositoryBase
import com.markettwits.starts.data.StartsCloudToUiMapper
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val startFilterModule = module {
    factoryOf(::StartFilterRepositoryBase) bind StartFilterRepository::class
    single<StartsCloudToUiMapper> {
        StartsCloudToUiMapper.Base(
            BaseTimeMapper()
        )
    }
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
        }
    )
}

