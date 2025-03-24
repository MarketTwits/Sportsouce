package com.markettwits.start_search.root.di

import com.markettwits.start.di.startModule
import com.markettwits.start_search.filter.di.StartsFilterDependencies
import com.markettwits.start_search.filter.di.startFilterModule
import com.markettwits.start_search.search.di.startsSearchModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val rootStartsSearchModule = module {
    singleOf(::StartsFilterDependenciesBase) bind StartsFilterDependencies::class
    includes(startsSearchModule, startModule, startFilterModule)
}

