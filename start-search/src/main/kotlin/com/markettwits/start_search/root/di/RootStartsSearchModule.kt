package com.markettwits.start_search.root.di

import com.markettwits.start.di.startModule
import com.markettwits.start_search.search.di.startsSearchModule
import org.koin.dsl.module

val rootStartsSearchModule = module {
    includes(startsSearchModule, startModule)
}
