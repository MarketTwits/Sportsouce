package com.markettwits.sportsouce.start.search.root.di

import com.markettwits.sportsouce.start.di.startModule
import com.markettwits.sportsouce.start.search.filter.di.StartsFilterDependencies
import com.markettwits.sportsouce.start.search.filter.di.startFilterModule
import com.markettwits.sportsouce.start.search.search.di.startsSearchModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val rootStartsSearchModule = module {
    singleOf(::StartsFilterDependenciesBase) bind StartsFilterDependencies::class
    includes(startsSearchModule, startModule, startFilterModule)
}

