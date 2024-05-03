package com.markettwits.start_search.search.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cahce.ObservableListCache
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.start_search.search.data.local.SearchHistoryCache
import com.markettwits.start_search.search.data.mapper.StartsSearchToUiMapper
import com.markettwits.start_search.search.data.mapper.StartsSearchToUiMapperBase
import com.markettwits.start_search.search.data.repository.StartsSearchRepository
import com.markettwits.start_search.search.data.repository.StartsSearchRepositoryBase
import com.markettwits.start_search.search.domain.SearchHistory
import com.markettwits.start_search.search.presentation.store.StartsSearchStoreFactory
import com.markettwits.starts_common.data.mapper.StartsCloudToListMapper
import com.markettwits.starts_common.data.mapper.StartsCloudToListMapperBase
import com.markettwits.time.BaseTimeMapper
import com.markettwits.time.TimeMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val startsSearchModule = module {
    includes(sportSouceNetworkModule)
    singleOf(::SearchHistoryCache)
    singleOf(::StartsSearchStoreFactory)
    singleOf(::StartsCloudToListMapperBase) bind StartsCloudToListMapper::class
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::StartsSearchToUiMapperBase) bind StartsSearchToUiMapper::class
    singleOf(::StartsSearchRepositoryBase) bind StartsSearchRepository::class
    singleOf(::BaseTimeMapper) bind TimeMapper::class
    single<ObservableListCache<SearchHistory>> {
        SearchHistoryCache()
    }
}