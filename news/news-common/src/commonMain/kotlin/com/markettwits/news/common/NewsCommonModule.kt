package com.markettwits.news.common

import com.markettwits.news.cloud.sportSauceNewsNetworkModule
import com.markettwits.news.common.internal.NetworkNewsMapper
import com.markettwits.news.common.internal.NewsRepositoryBase
import com.markettwits.time.BaseTimeMapper
import com.markettwits.time.TimeMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val newsCommonModule = module {
    includes(sportSauceNewsNetworkModule)
    singleOf(::NetworkNewsMapper)
    singleOf(::BaseTimeMapper) bind TimeMapper::class
    singleOf(::NewsRepositoryBase) bind NewsRepository::class
}