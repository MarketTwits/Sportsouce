package com.markettwits.sportsouce.start.search.search.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cahce.ObservableListCache
import com.markettwits.core.time.BaseTimeMapper
import com.markettwits.core.time.TimeMapper
import com.markettwits.sportsouce.start.cloud.di.sportSauceStartNetworkModule
import com.markettwits.sportsouce.start.search.filter.di.startFilterModule
import com.markettwits.sportsouce.start.search.search.data.local.SearchHistoryCache
import com.markettwits.sportsouce.start.search.search.data.mapper.StartsSearchToUiMapper
import com.markettwits.sportsouce.start.search.search.data.mapper.StartsSearchToUiMapperBase
import com.markettwits.sportsouce.start.search.search.data.repository.StartsSearchRepository
import com.markettwits.sportsouce.start.search.search.data.repository.StartsSearchRepositoryBase
import com.markettwits.sportsouce.start.search.search.domain.SearchHistory
import com.markettwits.sportsouce.start.search.search.presentation.store.StartsSearchStoreFactory
import com.markettwits.sportsouce.starts.common.di.startsCommonModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val startsSearchModule = module {
    includes(sportSauceStartNetworkModule, startFilterModule, startsCommonModule)
    singleOf(::SearchHistoryCache)
    singleOf(::StartsSearchStoreFactory)
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::StartsSearchToUiMapperBase) bind StartsSearchToUiMapper::class
    singleOf(::StartsSearchRepositoryBase) bind StartsSearchRepository::class
    singleOf(::BaseTimeMapper) bind TimeMapper::class
    single<ObservableListCache<SearchHistory>> {
        SearchHistoryCache()
    }
}