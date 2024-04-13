package com.markettwits.starts.starts.di

import com.markettwits.cahce.execute.list.ExecuteListWithCache
import com.markettwits.cahce.execute.list.ExecuteListWithCacheBase
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.starts.starts.data.StartsCloudToUiMapper
import com.markettwits.starts.starts.data.StartsMainCache
import com.markettwits.starts.starts.data.StartsRepositoryBase
import com.markettwits.starts.starts.domain.StartsRepository
import com.markettwits.starts_common.data.mapper.StartsCloudToListMapperBase
import com.markettwits.time.BaseTimeMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val startsModule = module {
    includes(sportSouceNetworkModule)

    singleOf(::StartsRepositoryBase) bind StartsRepository::class
    singleOf(::ExecuteListWithCacheBase) bind ExecuteListWithCache::class
    singleOf(::StartsMainCache)
    single<StartsCloudToUiMapper> {
        StartsCloudToUiMapper.Base(
            StartsCloudToListMapperBase(BaseTimeMapper())
        )
    }
}