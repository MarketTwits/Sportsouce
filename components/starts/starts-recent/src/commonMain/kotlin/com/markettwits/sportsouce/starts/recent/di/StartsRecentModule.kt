package com.markettwits.sportsouce.starts.recent.di

import com.markettwits.sportsouce.starts.recent.data.StartRecentRepositoryBase
import com.markettwits.sportsouce.starts.recent.data.StartsRecentCache
import com.markettwits.sportsouce.starts.recent.domain.StartRecentRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val startsRecentModule = module {
    singleOf(::StartsRecentCache)
    singleOf(::StartRecentRepositoryBase) bind StartRecentRepository::class
}
