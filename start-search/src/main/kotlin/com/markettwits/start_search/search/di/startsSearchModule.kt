package com.markettwits.start_search.search.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.core_ui.time.BaseTimeMapper
import com.markettwits.core_ui.time.TimeMapper
import com.markettwits.start_search.search.data.StartsSearchRepository
import com.markettwits.start_search.search.data.StartsSearchRepositoryBase
import com.markettwits.start_search.search.data.mapper.StartsSearchToUiMapper
import com.markettwits.start_search.search.data.mapper.StartsSearchToUiMapperBase
import com.markettwits.start_search.search.presentation.store.StartsSearchStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val startsSearchModule = module {
    includes(sportSouceNetworkModule)
    singleOf(::StartsSearchStoreFactory)
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::StartsSearchToUiMapperBase) bind StartsSearchToUiMapper::class
    singleOf(::StartsSearchRepositoryBase) bind StartsSearchRepository::class
    singleOf(::BaseTimeMapper) bind TimeMapper::class
}