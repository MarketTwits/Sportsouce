package com.markettwits.schedule.schedule.di

import com.markettwits.schedule.schedule.data.ScheduleRepository
import com.markettwits.schedule.schedule.data.ScheduleRepositoryBase
import com.markettwits.schedule.schedule.data.StartScheduleMapper
import com.markettwits.schedule.schedule.data.StartScheduleMapperBase
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val startScheduleModule = module {
    factoryOf(::ScheduleRepositoryBase) bind ScheduleRepository::class
    singleOf(::StartScheduleMapperBase) bind StartScheduleMapper::class
}

internal fun createStartRandomModule(dependencies: StartScheduleComponentDependencies): List<Module> {
    return listOf(
        startScheduleModule,
        module {
            factory { dependencies.startsCloudToUiMapper }
            factory { dependencies.sportsouceApi }
        }
    )
}