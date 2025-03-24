package com.markettwits.popular.popular.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.popular.popular.data.StartsPopularRepositoryBase
import com.markettwits.popular.popular.domain.RecentStartsFilter
import com.markettwits.popular.popular.domain.RecentStartsFilterBase
import com.markettwits.popular.popular.domain.StartsPopularRepository
import com.markettwits.popular.popular.presentation.store.StartsPopularStoreFactory
import com.markettwits.starts_common.di.startsCommonModule
import com.markettwits.time.BaseTimeMapper
import com.markettwits.time.TimeMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val popularStartsModule = module {
    includes(startsCommonModule)
    singleOf(::StartsPopularStoreFactory)
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::StartsPopularRepositoryBase) bind StartsPopularRepository::class
    singleOf(::BaseTimeMapper) bind TimeMapper::class
    singleOf(::RecentStartsFilterBase) bind RecentStartsFilter::class
}