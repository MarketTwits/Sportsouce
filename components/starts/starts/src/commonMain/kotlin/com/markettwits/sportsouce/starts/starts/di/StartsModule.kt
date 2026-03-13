package com.markettwits.sportsouce.starts.starts.di

import com.markettwits.crashlitics.api.di.crashlyticsModule
import com.markettwits.sportsouce.starts.common.di.startsCommonModule
import com.markettwits.sportsouce.starts.starts.data.StartsRepositoryBase
import com.markettwits.sportsouce.starts.starts.data.StartsTabsCache
import com.markettwits.sportsouce.starts.starts.domain.StartsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val startsModule = module {
    includes(startsCommonModule, crashlyticsModule)
    singleOf(::StartsRepositoryBase) bind StartsRepository::class
    singleOf(::StartsTabsCache)
}
