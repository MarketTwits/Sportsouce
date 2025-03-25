package com.markettwits.sportsouce.starts.random.root.di

import com.markettwits.sportsouce.starts.random.data.RandomRepository
import com.markettwits.sportsouce.starts.random.data.RandomRepositoryBase
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val startRandomModule = module {
    factoryOf(::RandomRepositoryBase) bind RandomRepository::class
}
internal fun createStartRandomModule(dependencies : StartRandomComponentDependencies) : List<Module> {
    return listOf(
        startRandomModule,
        module {
            factory { dependencies.sportsouceApi }
        }
    )
}