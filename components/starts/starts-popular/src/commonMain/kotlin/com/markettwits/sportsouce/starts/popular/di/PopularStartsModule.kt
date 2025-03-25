package com.markettwits.sportsouce.starts.popular.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.core.time.BaseTimeMapper
import com.markettwits.core.time.TimeMapper
import com.markettwits.sportsouce.starts.common.di.startsCommonModule
import com.markettwits.sportsouce.starts.popular.data.StartsPopularRepositoryBase
import com.markettwits.sportsouce.starts.popular.domain.RecentStartsFilter
import com.markettwits.sportsouce.starts.popular.domain.RecentStartsFilterBase
import com.markettwits.sportsouce.starts.popular.domain.StartsPopularRepository
import com.markettwits.sportsouce.starts.popular.presentation.store.StartsPopularStoreFactory
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