package com.markettwits.sportsouce.news.common

import com.markettwits.core.time.BaseTimeMapper
import com.markettwits.core.time.TimeMapper
import com.markettwits.sportsouce.news.cloud.sportSauceNewsNetworkModule
import com.markettwits.sportsouce.news.common.internal.NetworkNewsMapper
import com.markettwits.sportsouce.news.common.internal.NewsRepositoryBase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val newsCommonModule = module {
    includes(sportSauceNewsNetworkModule)
    singleOf(::NetworkNewsMapper)
    singleOf(::BaseTimeMapper) bind TimeMapper::class
    singleOf(::NewsRepositoryBase) bind NewsRepository::class
}