package com.markettwits.start_filter.start_filter.di

import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.core_ui.time.BaseTimeMapper
import com.markettwits.start_filter.start_filter.data.StartFilterDomainToRemoteMapper
import com.markettwits.start_filter.start_filter.data.StartFilterRemoteToDomainMapper
import com.markettwits.start_filter.start_filter.data.StartFilterRepository
import com.markettwits.start_filter.start_filter.data.StartFilterRepositoryBase
import com.markettwits.starts.data.StartsCloudToUiMapper
import org.koin.dsl.module

val startFilterModule = module {
    includes(sportSouceNetworkModule)
    single<StartFilterRepository> {
        StartFilterRepositoryBase(
            service = get(),
            fetchMapper = get(),
            sendMapper = get(),
            startsCloudToUiMapper = StartsCloudToUiMapper.Base(BaseTimeMapper())
        )
    }
    single<StartFilterRemoteToDomainMapper> {
        StartFilterRemoteToDomainMapper.Base()
    }
    single<StartFilterDomainToRemoteMapper> {
        StartFilterDomainToRemoteMapper.Base()
    }
}