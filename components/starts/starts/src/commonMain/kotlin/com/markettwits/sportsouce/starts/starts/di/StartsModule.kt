package com.markettwits.sportsouce.starts.starts.di

import com.markettwits.cahce.execute.list.ExecuteListWithCache
import com.markettwits.cahce.execute.list.ExecuteListWithCacheBase
import com.markettwits.sportsouce.starts.common.di.startsCommonModule
import com.markettwits.sportsouce.starts.starts.data.StartsCloudToUiMapper
import com.markettwits.sportsouce.starts.starts.data.StartsMainCache
import com.markettwits.sportsouce.starts.starts.data.StartsRepositoryBase
import com.markettwits.sportsouce.starts.starts.domain.StartsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val startsModule = module {
    includes(startsCommonModule)
    singleOf(::StartsRepositoryBase) bind StartsRepository::class
    singleOf(::ExecuteListWithCacheBase) bind ExecuteListWithCache::class
    singleOf(::StartsMainCache)
    single<StartsCloudToUiMapper> {
        StartsCloudToUiMapper.Base()
    }
}