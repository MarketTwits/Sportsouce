package com.markettwits.popular.popular.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.core_ui.time.BaseTimeMapper
import com.markettwits.core_ui.time.TimeMapper
import com.markettwits.popular.popular.data.StartsPopularRepositoryBase
import com.markettwits.popular.popular.domain.RecentStartsFilter
import com.markettwits.popular.popular.domain.RecentStartsFilterBase
import com.markettwits.popular.popular.domain.StartsPopularRepository
import com.markettwits.popular.popular.presentation.store.StartsPopularStoreFactory
import com.markettwits.starts_common.data.mapper.StartsCloudToListMapper
import com.markettwits.starts_common.data.mapper.StartsCloudToListMapperBase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val popularStartsModule = module {
    includes(sportSouceNetworkModule)
    singleOf(::StartsPopularStoreFactory)
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::StartsPopularRepositoryBase) bind StartsPopularRepository::class
    singleOf(::StartsCloudToListMapperBase) bind StartsCloudToListMapper::class
    singleOf(::BaseTimeMapper) bind TimeMapper::class
    singleOf(::RecentStartsFilterBase) bind RecentStartsFilter::class
}